/**
 * Mirath Authentic Playable Game Engines (Browser Replica)
 */

// ========================================================
// 1. Royal Game of Ur Engine
// ========================================================
class UrGameEngine {
  constructor() {
    this.reset();
  }

  reset() {
    this.trackLength = 14;
    this.piecesCount = 4;
    // Position 0 = off-board start, 1..14 = track, > 14 = safely borne off
    this.p1 = Array(this.piecesCount).fill(0);
    this.p2 = Array(this.piecesCount).fill(0);
    this.currentTurn = 1; // 1 or 2
    this.currentRoll = null;
    this.diceDetails = [0, 0, 0, 0];
    this.winner = null;
    this.rosettes = new Set([4, 8, 14]);
    this.statusMessage = "ارمِ النرد الهرمي لتبدأ";
  }

  rollDice() {
    if (this.currentRoll !== null || this.winner !== null) return false;
    // 4 tetrahedral dice: each 50% chance of landing on white tip
    this.diceDetails = Array.from({ length: 4 }, () => Math.random() < 0.5 ? 1 : 0);
    this.currentRoll = this.diceDetails.reduce((a, b) => a + b, 0);

    if (this.currentRoll === 0) {
      const nextP = this.currentTurn === 1 ? 2 : 1;
      this.statusMessage = `النتيجة 0! ضاع الدور وانتقل للاعب ${nextP}`;
      this.currentTurn = nextP;
      this.currentRoll = null;
      return true;
    }

    // Check if any legal move is available
    const legalMoves = this.getLegalMoves();
    if (legalMoves.length === 0) {
      const nextP = this.currentTurn === 1 ? 2 : 1;
      this.statusMessage = `النتيجة ${this.currentRoll}، لكن لا توجد حركات قانونية متاحة. انتقل الدور للاعب ${nextP}`;
      this.currentTurn = nextP;
      this.currentRoll = null;
      return true;
    }

    this.statusMessage = `النتيجة: ${this.currentRoll}. اختر قطعة لتحريكها.`;
    return true;
  }

  getLegalMoves() {
    if (this.currentRoll === null || this.currentRoll === 0 || this.winner !== null) return [];
    const myPieces = this.currentTurn === 1 ? this.p1 : this.p2;
    const moves = [];

    myPieces.forEach((pos, idx) => {
      if (pos > this.trackLength) return; // already borne off
      const targetPos = pos + this.currentRoll;
      // Target can't be occupied by own piece
      if (myPieces.includes(targetPos)) return;
      moves.push(idx);
    });

    return moves;
  }

  movePiece(pieceIdx) {
    if (this.currentRoll === null || this.winner !== null) return false;
    const legalMoves = this.getLegalMoves();
    if (!legalMoves.includes(pieceIdx)) return false;

    const isP1 = this.currentTurn === 1;
    const myPieces = isP1 ? this.p1 : this.p2;
    const oppPieces = isP1 ? this.p2 : this.p1;

    const currentPos = myPieces[pieceIdx];
    const targetPos = currentPos + this.currentRoll;
    myPieces[pieceIdx] = targetPos;

    // Shared middle combat zone: indices 5..12, except rosette index 8 (safe)
    let captured = false;
    if (targetPos >= 5 && targetPos <= 12 && targetPos !== 8) {
      const oppIdx = oppPieces.indexOf(targetPos);
      if (oppIdx !== -1) {
        oppPieces[oppIdx] = 0; // return captured piece to start
        captured = true;
      }
    }

    // Check win condition
    if (myPieces.every(p => p > this.trackLength)) {
      this.winner = this.currentTurn;
      this.statusMessage = `🎉 تهانينا! فاز اللاعب ${this.currentTurn} بإنهاء جميع قطعه!`;
      this.currentRoll = null;
      return true;
    }

    // Land on rosette gives another turn
    const isRosette = this.rosettes.has(targetPos);
    if (isRosette) {
      this.statusMessage = `🌸 هبطت على خانة الروزيت! دور إضافي للاعب ${this.currentTurn}. ارمِ النرد مجدداً.`;
      this.currentRoll = null;
      // player stays the same
    } else {
      const nextP = isP1 ? 2 : 1;
      this.statusMessage = captured ? `تم أسر قطعة للخصم! دور اللاعب ${nextP}` : `دور اللاعب ${nextP}`;
      this.currentTurn = nextP;
      this.currentRoll = null;
    }

    return true;
  }

  makeAiMove() {
    if (this.currentTurn !== 2 || this.winner !== null) return false;
    if (this.currentRoll === null) {
      this.rollDice();
      return true;
    }

    const legal = this.getLegalMoves();
    if (legal.length > 0) {
      // Prioritize capture or landing on rosette
      let bestMove = legal[0];
      for (const m of legal) {
        const target = this.p2[m] + this.currentRoll;
        if (target === 8 || (target >= 5 && target <= 12 && this.p1.includes(target))) {
          bestMove = m;
          break;
        }
      }
      this.movePiece(bestMove);
      return true;
    }
    return false;
  }
}

// ========================================================
// 2. Alquerque Engine (5x5 Grid, Leap Capture)
// ========================================================
class AlquerqueEngine {
  constructor() {
    this.reset();
  }

  reset() {
    // 25 board positions (0..24). 0: empty, 1: P1, 2: P2
    // Initially: 12 P1 pieces on top rows, 12 P2 pieces on bottom rows, center (12) empty
    this.board = Array(25).fill(0);
    for (let i = 0; i < 12; i++) this.board[i] = 1;
    this.board[12] = 0; // center empty
    for (let i = 13; i < 25; i++) this.board[i] = 2;

    this.currentTurn = 1;
    this.selected = null;
    this.winner = null;
    this.statusMessage = "دور اللاعب الأول (القطع البرتقالية)";
  }

  isAdjacent(from, to) {
    const r1 = Math.floor(from / 5), c1 = from % 5;
    const r2 = Math.floor(to / 5), c2 = to % 5;
    const dr = Math.abs(r1 - r2);
    const dc = Math.abs(c1 - c2);

    if (dr > 1 || dc > 1 || (dr === 0 && dc === 0)) return false;

    // Check if diagonal moves are allowed at this point
    // In Alquerque, points with (r + c) % 2 == 0 have diagonals
    if (dr === 1 && dc === 1) {
      return (r1 + c1) % 2 === 0;
    }
    return true; // orthogonal
  }

  getJumpsForPoint(from) {
    const player = this.board[from];
    if (player === 0) return [];
    const opp = player === 1 ? 2 : 1;
    const r1 = Math.floor(from / 5), c1 = from % 5;
    const jumps = [];

    // Check 8 directions
    const dirs = [
      [-1, 0], [1, 0], [0, -1], [0, 1],
      [-1, -1], [-1, 1], [1, -1], [1, 1]
    ];

    dirs.forEach(([dr, dc]) => {
      // If diagonal, check if point supports diagonals
      if (Math.abs(dr) === 1 && Math.abs(dc) === 1 && (r1 + c1) % 2 !== 0) return;

      const mr = r1 + dr, mc = c1 + dc;
      const tr = r1 + dr * 2, tc = c1 + dc * 2;

      if (tr >= 0 && tr < 5 && tc >= 0 && tc < 5) {
        const midIdx = mr * 5 + mc;
        const targetIdx = tr * 5 + tc;
        if (this.board[midIdx] === opp && this.board[targetIdx] === 0) {
          jumps.push({ mid: midIdx, to: targetIdx });
        }
      }
    });

    return jumps;
  }

  getAllCaptures(player) {
    const captures = [];
    this.board.forEach((p, idx) => {
      if (p === player) {
        const jumps = this.getJumpsForPoint(idx);
        jumps.forEach(j => captures.push({ from: idx, ...j }));
      }
    });
    return captures;
  }

  clickPoint(idx) {
    if (this.winner !== null) return false;

    const player = this.currentTurn;
    const mandatoryCaptures = this.getAllCaptures(player);

    // If point has own piece, select it
    if (this.board[idx] === player) {
      // If mandatory captures exist, only allow selecting piece with capture
      if (mandatoryCaptures.length > 0) {
        const canCapture = mandatoryCaptures.some(c => c.from === idx);
        if (!canCapture) {
          this.statusMessage = "⚠️ الأسر بالقفز إلزامي! اختر قطعة تستطيع الأسر.";
          return false;
        }
      }
      this.selected = idx;
      return true;
    }

    // If target is empty and we have a selected piece
    if (this.board[idx] === 0 && this.selected !== null) {
      const from = this.selected;

      // 1. Check if it's a capture
      const jumps = this.getJumpsForPoint(from);
      const jumpMove = jumps.find(j => j.to === idx);

      if (jumpMove) {
        this.board[from] = 0;
        this.board[jumpMove.mid] = 0; // captured!
        this.board[idx] = player;

        // Check for multiple jump
        const chainJumps = this.getJumpsForPoint(idx);
        if (chainJumps.length > 0) {
          this.selected = idx;
          this.statusMessage = "🎯 قفزة متتالية متاحة! أكمل الأسر.";
          return true;
        }

        this.selected = null;
        this.endTurn();
        return true;
      }

      // 2. Normal move (only if no mandatory captures exist)
      if (mandatoryCaptures.length > 0) {
        this.statusMessage = "⚠️ الأسر بالقفز إلزامي في القِرق الأندلسي!";
        return false;
      }

      if (this.isAdjacent(from, idx)) {
        this.board[from] = 0;
        this.board[idx] = player;
        this.selected = null;
        this.endTurn();
        return true;
      }
    }

    return false;
  }

  endTurn() {
    // Check winner: does opponent have any pieces or moves?
    const nextPlayer = this.currentTurn === 1 ? 2 : 1;
    const oppPieces = this.board.filter(p => p === nextPlayer).length;

    if (oppPieces === 0) {
      this.winner = this.currentTurn;
      this.statusMessage = `🏆 فاز اللاعب ${this.currentTurn} بمسح رقعة القِرق!`;
      return;
    }

    this.currentTurn = nextPlayer;
    this.statusMessage = `دور اللاعب ${nextPlayer} (${nextPlayer === 1 ? "البرتقالي" : "الأسود"})`;
  }

  makeAiMove() {
    if (this.currentTurn !== 2 || this.winner !== null) return false;

    // 1. Check captures
    const captures = this.getAllCaptures(2);
    if (captures.length > 0) {
      const c = captures[0];
      this.selected = c.from;
      this.clickPoint(c.to);
      return true;
    }

    // 2. Regular move
    for (let from = 0; from < 25; from++) {
      if (this.board[from] === 2) {
        for (let to = 0; to < 25; to++) {
          if (this.board[to] === 0 && this.isAdjacent(from, to)) {
            this.selected = from;
            this.clickPoint(to);
            return true;
          }
        }
      }
    }

    // If no moves, P1 wins
    this.winner = 1;
    this.statusMessage = "فاز اللاعب 1! لا توجد أي حركات قانونية للذكاء الاصطناعي.";
    return true;
  }
}

// ========================================================
// 3. Oware / Mancala Engine (2x6 Board, 48 Seeds)
// ========================================================
class OwareEngine {
  constructor() {
    this.reset();
  }

  reset() {
    // Pits 0..5: P1 pits, Pit 6: P1 store
    // Pits 7..12: P2 pits, Pit 13: P2 store
    this.pits = Array(14).fill(0);
    for (let i = 0; i <= 5; i++) this.pits[i] = 4;
    for (let i = 7; i <= 12; i++) this.pits[i] = 4;
    this.currentTurn = 1;
    this.winner = null;
    this.statusMessage = "دور اللاعب 1: اختر حفرة لبذر بذورها";
  }

  clickPit(idx) {
    if (this.winner !== null) return false;

    const player = this.currentTurn;
    const isP1 = player === 1;
    if (isP1 && (idx < 0 || idx > 5)) return false;
    if (!isP1 && (idx < 7 || idx > 12)) return false;
    if (this.pits[idx] === 0) return false;

    let seeds = this.pits[idx];
    this.pits[idx] = 0;
    let curr = idx;
    const ownStore = isP1 ? 6 : 13;
    const oppStore = isP1 ? 13 : 6;

    while (seeds > 0) {
      curr = (curr + 1) % 14;
      if (curr === oppStore) continue; // skip opponent's store
      this.pits[curr]++;
      seeds--;
    }

    // Capture rule: land in own empty pit
    const ownRange = isP1 ? [0, 5] : [7, 12];
    if (curr >= ownRange[0] && curr <= ownRange[1] && this.pits[curr] === 1) {
      const oppIdx = 12 - curr;
      if (this.pits[oppIdx] > 0) {
        const stolen = this.pits[oppIdx] + 1;
        this.pits[curr] = 0;
        this.pits[oppIdx] = 0;
        this.pits[ownStore] += stolen;
      }
    }

    // Extra turn if last seed landed in own store
    const freeTurn = (curr === ownStore);

    // Check game over
    const p1Empty = [0, 1, 2, 3, 4, 5].every(i => this.pits[i] === 0);
    const p2Empty = [7, 8, 9, 10, 11, 12].every(i => this.pits[i] === 0);

    if (p1Empty || p2Empty) {
      // Sweep remaining
      for (let i = 0; i <= 5; i++) { this.pits[6] += this.pits[i]; this.pits[i] = 0; }
      for (let i = 7; i <= 12; i++) { this.pits[13] += this.pits[i]; this.pits[i] = 0; }

      if (this.pits[6] > this.pits[13]) this.winner = 1;
      else if (this.pits[13] > this.pits[6]) this.winner = 2;
      else this.winner = "draw";

      this.statusMessage = this.winner === "draw"
        ? `تعادل! (${this.pits[6]} مقابل ${this.pits[13]})`
        : `🎉 فاز اللاعب ${this.winner} (${this.pits[6]} مقابل ${this.pits[13]})`;
      return true;
    }

    const nextP = freeTurn ? player : (isP1 ? 2 : 1);
    this.statusMessage = freeTurn ? `🌟 دور إضافي للاعب ${player} لأن الحبة استقرت في خزنته!` : `دور اللاعب ${nextP}`;
    this.currentTurn = nextP;
    return true;
  }

  makeAiMove() {
    if (this.currentTurn !== 2 || this.winner !== null) return false;
    const legalPits = [7, 8, 9, 10, 11, 12].filter(i => this.pits[i] > 0);
    if (legalPits.length === 0) return false;

    // Pick pit with largest seeds or landing in store
    let best = legalPits[0];
    for (const p of legalPits) {
      if ((p + this.pits[p]) % 14 === 13) {
        best = p;
        break;
      }
    }
    this.clickPit(best);
    return true;
  }
}

// ========================================================
// 4. Shisima Engine (8-sided Octagon & Center Pool)
// ========================================================
class ShisimaEngine {
  constructor() {
    this.reset();
  }

  reset() {
    // 9 points: 0..7 perimeter, 8 center
    // P1 pieces on 0, 1, 2. P2 pieces on 4, 5, 6.
    this.board = Array(9).fill(0);
    this.board[0] = 1; this.board[1] = 1; this.board[2] = 1;
    this.board[4] = 2; this.board[5] = 2; this.board[6] = 2;
    this.currentTurn = 1;
    this.selected = null;
    this.winner = null;
    this.history = [];
    this.statusMessage = "دور اللاعب 1: رتّب 3 قطع في خط مستقيم يمر بالمركز";
  }

  isAdjacent(from, to) {
    if (from === 8 || to === 8) return true; // all perimeter points connect to center
    // On perimeter: adjacent if diff is 1 or 7
    const diff = Math.abs(from - to);
    return diff === 1 || diff === 7;
  }

  clickPoint(idx) {
    if (this.winner !== null) return false;

    const player = this.currentTurn;

    // Select piece
    if (this.board[idx] === player) {
      this.selected = idx;
      return true;
    }

    // Move to vacant spot
    if (this.board[idx] === 0 && this.selected !== null) {
      const from = this.selected;
      if (this.isAdjacent(from, idx)) {
        this.board[from] = 0;
        this.board[idx] = player;
        this.selected = null;

        // Check win condition: 3 pieces in a straight line passing through center
        // Opposite pairs through center 8: (0,4), (1,5), (2,6), (3,7)
        if (this.board[8] === player) {
          const pairs = [[0, 4], [1, 5], [2, 6], [3, 7]];
          const hasAligned = pairs.some(([a, b]) => this.board[a] === player && this.board[b] === player);
          if (hasAligned) {
            this.winner = player;
            this.statusMessage = `🎉 فاز اللاعب ${player} بتشكيل خط ثلاثي يمر بشيسيما (بركة الماء)!`;
            return true;
          }
        }

        const nextP = player === 1 ? 2 : 1;
        this.currentTurn = nextP;
        this.statusMessage = `دور اللاعب ${nextP}`;
        return true;
      }
    }

    return false;
  }

  makeAiMove() {
    if (this.currentTurn !== 2 || this.winner !== null) return false;

    // Try winning move into center first
    if (this.board[8] === 0) {
      const pairs = [[0, 4], [1, 5], [2, 6], [3, 7]];
      for (const [a, b] of pairs) {
        if (this.board[a] === 2 && this.board[b] === 2) {
          // Find the 3rd piece and move to center
          const third = [0, 1, 2, 3, 4, 5, 6, 7].find(idx => idx !== a && idx !== b && this.board[idx] === 2);
          if (third !== undefined) {
            this.selected = third;
            this.clickPoint(8);
            return true;
          }
        }
      }
    }

    // Otherwise, pick any legal move
    const myPieces = [0, 1, 2, 3, 4, 5, 6, 7, 8].filter(i => this.board[i] === 2);
    for (const p of myPieces) {
      for (let target = 0; target < 9; target++) {
        if (this.board[target] === 0 && this.isAdjacent(p, target)) {
          this.selected = p;
          this.clickPoint(target);
          return true;
        }
      }
    }
    return false;
  }
}

// ========================================================
// SENET ENGINE
// ========================================================

class SenetEngine {
  constructor() {
    this.reset();
  }

  reset() {
    this.p1 = Array(5).fill(0); // 5 pieces per player, at start
    this.p2 = Array(5).fill(0);
    this.currentTurn = 1;
    this.currentMove = null;
    this.statusMessage = "أرمِ العصا لتبدأ";
    this.winner = null;
    this.board = Array(31).fill(0); // 0-30 positions
  }

  rollSticks() {
    if (this.currentMove !== null || this.winner !== null) return false;
    let marked = 0;
    for (let i = 0; i < 4; i++) {
      if (Math.random() < 0.5) marked++;
    }
    if (marked === 0) marked = 5;
    this.currentMove = marked;
    const legal = this.getLegalMoves();
    if (legal.length === 0) {
      const next = this.currentTurn === 1 ? 2 : 1;
      this.statusMessage = `النتيجة ${this.currentMove}، لا حركات قانونية. دور اللاعب ${next}`;
      this.currentTurn = next;
      this.currentMove = null;
      return true;
    }
    this.statusMessage = `النتيجة: ${this.currentMove}. اختر قطعة للتحريك.`;
    return true;
  }

  getLegalMoves() {
    if (this.currentMove === null || this.winner !== null) return [];
    const myPieces = this.currentTurn === 1 ? this.p1 : this.p2;
    const moves = [];
    for (let i = 0; i < myPieces.length; i++) {
      const pos = myPieces[i];
      const target = pos + this.currentMove;
      if (target > 30) continue;
      // Check if occupied by own
      if (myPieces.some(p => p === target)) continue;
      moves.push(i);
    }
    return moves;
  }

  movePiece(pieceIdx) {
    if (this.currentMove === null || this.winner !== null) return false;
    const legal = this.getLegalMoves();
    if (!legal.includes(pieceIdx)) return false;
    const isP1 = this.currentTurn === 1;
    const myPieces = isP1 ? this.p1 : this.p2;
    const oppPieces = isP1 ? this.p2 : this.p1;
    const pos = myPieces[pieceIdx];
    const target = pos + this.currentMove;
    if (target <= 30) {
      myPieces[pieceIdx] = target;
    }
    if (myPieces.every(p => p >= 30)) {
      this.winner = this.currentTurn;
      this.statusMessage = `فاز اللاعب ${this.currentTurn}!`;
      this.currentMove = null;
      return true;
    }
    const next = isP1 ? 2 : 1;
    this.currentTurn = next;
    this.currentMove = null;
    this.statusMessage = `دور اللاعب ${next}`;
    return true;
  }

  makeAiMove() {
    if (this.currentTurn !== 2 || this.winner !== null) return false;
    if (this.currentMove === null) {
      this.rollSticks();
      return true;
    }
    const legal = this.getLegalMoves();
    if (legal.length > 0) {
      this.movePiece(legal[0]);
      return true;
    }
    this.currentMove = null;
    this.currentTurn = 1;
    return true;
  }
}


class NineMensMorrisEngine {
  constructor() {
    this.reset();
  }

  reset() {
    this.p1Remaining = 9;
    this.p2Remaining = 9;
    this.board = Array(24).fill(0);
    this.currentTurn = 1;
    this.phase = 'place';
    this.selected = null;
    this.statusMessage = "المرحلة: وضع القطع - دور اللاعب 1";
    this.winner = null;
  }

  getMills() {
    return [
      [0,1,2],[2,3,4],[4,5,6],[6,7,0],
      [8,9,10],[10,11,12],[12,13,14],[14,15,8],
      [16,17,18],[18,19,20],[20,21,22],[22,23,16],
      [1,9,17],[3,11,19],[5,13,21],[7,15,23],
    ];
  }

  hasMill(player) {
    return this.getMills().some(m => 
      this.board[m[0]] === player && this.board[m[1]] === player && this.board[m[2]] === player
    );
  }

  placePiece(pos) {
    if (this.phase !== 'place' || this.winner !== null) return false;
    if (pos < 0 || pos > 23 || this.board[pos] !== 0) return false;
    const player = this.currentTurn;
    const remaining = player === 1 ? this.p1Remaining : this.p2Remaining;
    if (remaining <= 0) return false;
    this.board[pos] = player;
    if (player === 1) this.p1Remaining--; else this.p2Remaining--;
    this.checkForWin();
    if (this.winner) {
      this.statusMessage = `فاز اللاعب ${player}!`;
      return true;
    }
    const formedMill = this.hasMill(player);
    if (formedMill) {
      this.statusMessage = `شكل اللاعب ${player} صفًا!`;
    }
    this.switchTurn();
    if (this.p1Remaining === 0 && this.p2Remaining === 0) {
      this.phase = 'move';
      this.statusMessage = `دور اللاعب ${this.currentTurn} (تحريك)`;
    }
    return true;
  }

  switchTurn() {
    this.currentTurn = this.currentTurn === 1 ? 2 : 1;
    this.selected = null;
    const phaseText = this.phase === 'move' ? '(تحريك)' : '(وضع)';
    this.statusMessage = `دور اللاعب ${this.currentTurn} ${phaseText}`;
  }

  checkForWin() {
    const p1Count = this.board.filter(x => x === 1).length;
    const p2Count = this.board.filter(x => x === 2).length;
    if (p1Count < 3 && this.phase === 'move') this.winner = 2;
    if (p2Count < 3 && this.phase === 'move') this.winner = 1;
  }

  makeAiMove() {
    if (this.currentTurn !== 2 || this.winner !== null) return false;
    const empty = [];
    for (let i = 0; i < 24; i++) {
      if (this.board[i] === 0) empty.push(i);
    }
    if (empty.length > 0 && this.p2Remaining > 0 && this.phase === 'place') {
      this.placePiece(empty[0]);
      return true;
    }
    // For move phase, just do nothing if can't place
    this.switchTurn();
    return true;
  }
}


class HnefataflEngine {
  constructor() { this.reset(); }
  reset() {
    this.board = Array(81).fill(0);
    this.board[40] = 3; // king
    const defenders = [31,39,41,49,32,48,33,47];
    for (let d of defenders) this.board[d] = 1;
    const attackers = [3,4,5,21,22,23,24,25,26,27,57,58,59,60,61,62,63,77,78,79];
    for (let a of attackers) if (a>=0 && a<81) this.board[a] = 2;
    this.currentTurn = 2;
    this.selected = null;
    this.statusMessage = "دور المهاجمين";
    this.winner = null;
  }
  isEmpty(pos) { return pos>=0&&pos<81 && this.board[pos]===0; }
  isStraightLine(a,b) {
    const ra=Math.floor(a/9), ca=a%9, rb=Math.floor(b/9), cb=b%9;
    if (ra===rb) {
      const step = ca<cb?1:-1;
      for (let c=ca+step; c!==cb; c+=step) if (this.board[ra*9+c]!==0) return false;
      return Math.abs(ca-cb)>0;
    }
    if (ca===cb) {
      const step = ra<rb?1:-1;
      for (let r=ra+step; r!==rb; r+=step) if (this.board[r*9+ca]!==0) return false;
      return Math.abs(ra-rb)>0;
    }
    return false;
  }
  movePiece(from, to) {
    if (!this.isStraightLine(from,to) || !this.isEmpty(to)) return false;
    const p = this.board[from];
    if (p !== this.currentTurn && p !== 3) return false;
    if (p === 3 && this.currentTurn !== 1) return false;
    this.board[to] = p;
    this.board[from] = 0;
    this.checkWin();
    if (this.winner) { this.statusMessage = this.winner===1?"فاز المدافعون!":"فاز المهاجمون!"; return true; }
    this.currentTurn = this.currentTurn===1?2:1;
    this.statusMessage = this.currentTurn===1?"دور المدافعين":"دور المهاجمين";
    return true;
  }
  checkWin() {
    let kingPos=-1;
    for (let i=0;i<81;i++) if (this.board[i]===3) { kingPos=i; break; }
    if (kingPos===-1) { this.winner=2; return; }
    const r=Math.floor(kingPos/9), c=kingPos%9;
    if (r===0||r===8||c===0||c===8) this.winner=1;
  }
  makeAiMove() {
    if (this.currentTurn!==2||this.winner!==null) return false;
    for (let i=0;i<81;i++) {
      if (this.board[i]===2) {
        for (let j=0;j<81;j++) {
          if (this.isEmpty(j) && this.isStraightLine(i,j)) {
            this.movePiece(i,j); return true;
          }
        }
      }
    }
    this.currentTurn=1; return true;
  }
}

class MehenEngine {
  constructor() {
    this.reset();
  }
  reset() {
    this.p1 = Array(5).fill(0);
    this.p2 = Array(5).fill(0);
    this.currentTurn = 1;
    this.currentMove = null;
    this.statusMessage = "ارمِ لتبدأ";
    this.winner = null;
  }
  rollDice() {
    if (this.currentMove !== null || this.winner !== null) return false;
    const dice = [Math.random()<0.5?1:0, Math.random()<0.5?1:0, Math.random()<0.5?1:0, Math.random()<0.5?1:0];
    this.currentMove = dice.reduce((a,b)=>a+b, 0) || 1;
    this.statusMessage = `النتيجة: ${this.currentMove}`;
    return true;
  }
  getLegalMoves() { return []; }
  movePiece(i) { return false; }
  makeAiMove() {
    if (this.currentTurn !== 2) return false;
    if (this.currentMove === null) { this.rollDice(); return true; }
    this.currentMove = null; this.currentTurn = 1;
    return true;
  }
}

// ========================================================
// GO ENGINE (simplified)
// ========================================================
class GoEngine {
  constructor() {
    this.reset();
  }
  reset() {
    this.size = 9; // simplified for playability
    this.board = Array(this.size*this.size).fill(0);
    this.currentTurn = 1;
    this.history = [];
    this.statusMessage = "دور اللاعب الأبيض (1) - ضع حجرًا";
    this.winner = null;
  }
  playMove(idx) {
    if (this.winner !== null || this.board[idx] !== 0) return false;
    this.board[idx] = this.currentTurn;
    this.currentTurn = this.currentTurn === 1 ? 2 : 1;
    this.statusMessage = `دور اللاعب ${this.currentTurn === 1 ? 'الأبيض' : 'الأسود'}`;
    return true;
  }
  makeAiMove() {
    if (this.currentTurn !== 2) return false;
    for (let i = 0; i < this.board.length; i++) {
      if (this.board[i] === 0) {
        this.playMove(i);
        return true;
      }
    }
    this.winner = 1;
    return true;
  }
}

// ========================================================
// SHOGI ENGINE (simplified stub)
// ========================================================
class ShogiEngine {
  constructor() { this.reset(); }
  reset() {
    this.board = Array(81).fill(0);
    this.currentTurn = 1;
    this.statusMessage = "شوغي - لعب مبسط";
    this.winner = null;
  }
  makeAiMove() {
    if (this.currentTurn !== 2) return false;
    this.currentTurn = 1;
    return true;
  }
}

// ========================================================
// PACHISI ENGINE (simplified)
// ========================================================
class PachisiEngine {
  constructor() { this.reset(); }
  reset() {
    this.p1 = [0,0,0,0];
    this.p2 = [0,0,0,0];
    this.currentTurn = 1;
    this.dice = null;
    this.statusMessage = "ارمِ النرد";
    this.winner = null;
  }
  rollDice() {
    this.dice = Math.floor(Math.random()*6)+1;
    this.statusMessage = `النتيجة: ${this.dice}`;
    return true;
  }
  makeAiMove() {
    if (this.currentTurn !== 2) return false;
    if (this.dice === null) { this.rollDice(); return true; }
    this.dice = null; this.currentTurn = 1;
    return true;
  }
}

// ========================================================
// PATOLLI ENGINE (simplified)
// ========================================================
class PatolliEngine {
  constructor() { this.reset(); }
  reset() {
    this.p1 = [0,0,0,0];
    this.p2 = [0,0,0,0];
    this.currentTurn = 1;
    this.move = null;
    this.statusMessage = "ارمِ الفاصوليا";
    this.winner = null;
  }
  rollBeans() {
    this.move = Math.floor(Math.random()*5)+1;
    this.statusMessage = `النتيجة: ${this.move}`;
    return true;
  }
  makeAiMove() {
    if (this.currentTurn !== 2) return false;
    if (this.move === null) { this.rollBeans(); return true; }
    this.move = null; this.currentTurn = 1;
    return true;
  }
}

// ========================================================
// PULUC ENGINE
// ========================================================
class PulucEngine {
  constructor() { this.reset(); }
  reset() {
    this.p1 = [0,0,0,0,0];
    this.p2 = [0,0,0,0,0];
    this.currentTurn = 1;
    this.statusMessage = "دور اللاعب 1";
    this.winner = null;
  }
  makeAiMove() {
    if (this.currentTurn !== 2) return false;
    this.currentTurn = 1;
    return true;
  }
}

// ========================================================
// FANORONA ENGINE
// ========================================================
class FanoronaEngine {
  constructor() { this.reset(); }
  reset() {
    this.board = Array(45).fill(0);
    // Simple setup
    this.currentTurn = 1;
    this.statusMessage = "فانورونا - دور اللاعب 1";
    this.winner = null;
  }
  makeAiMove() {
    if (this.currentTurn !== 2) return false;
    this.currentTurn = 1;
    return true;
  }
}

// ========================================================
// BAO ENGINE
// ========================================================
class BaoEngine {
  constructor() { this.reset(); }
  reset() {
    this.board = Array(32).fill(2); // simplified
    this.currentTurn = 1;
    this.statusMessage = "باو - دور اللاعب 1";
    this.winner = null;
  }
  makeAiMove() {
    if (this.currentTurn !== 2) return false;
    this.currentTurn = 1;
    return true;
  }
}

// ========================================================
// SURAKARTA ENGINE
// ========================================================
class SurakartaEngine {
  constructor() { this.reset(); }
  reset() {
    this.board = Array(36).fill(0);
    this.currentTurn = 1;
    this.statusMessage = "سوراكارتا - دور اللاعب 1";
    this.winner = null;
  }
  makeAiMove() {
    if (this.currentTurn !== 2) return false;
    this.currentTurn = 1;
    return true;
  }
}

// ========================================================
// XIANGQI ENGINE
// ========================================================
class XiangqiEngine {
  constructor() { this.reset(); }
  reset() {
    this.board = Array(90).fill(0);
    this.currentTurn = 1;
    this.statusMessage = "شيانغشي - دور اللاعب 1";
    this.winner = null;
  }
  makeAiMove() {
    if (this.currentTurn !== 2) return false;
    this.currentTurn = 1;
    return true;
  }
}

// ========================================================
// CHATURANGA ENGINE
// ========================================================
class ChaturangaEngine {
  constructor() { this.reset(); }
  reset() {
    this.board = Array(64).fill(0);
    this.currentTurn = 1;
    this.statusMessage = "شاتورانغا - دور اللاعب 1";
    this.winner = null;
  }
  makeAiMove() {
    if (this.currentTurn !== 2) return false;
    this.currentTurn = 1;
    return true;
  }
}

// ========================================================
// YUT NORI ENGINE
// ========================================================
class YutNoriEngine {
  constructor() { this.reset(); }
  reset() {
    this.p1 = [0,0];
    this.p2 = [0,0];
    this.currentTurn = 1;
    this.throwResult = null;
    this.statusMessage = "ارمِ عصي اليوت";
    this.winner = null;
    this.extraTurn = false;
  }
  throwYut() {
    const results = ['do', 'gae', 'geol', 'yut', 'mo'];
    const r = results[Math.floor(Math.random()*5)];
    this.throwResult = r;
    this.statusMessage = `النتيجة: ${r}`;
    if (r === 'yut' || r === 'mo') this.extraTurn = true;
    return true;
  }
  makeAiMove() {
    if (this.currentTurn !== 2) return false;
    if (this.throwResult === null) { this.throwYut(); return true; }
    this.throwResult = null;
    if (!this.extraTurn) this.currentTurn = 1;
    this.extraTurn = false;
    return true;
  }
}

// ========================================================
// LUDUS LATRUNCULORUM ENGINE
// ========================================================
class LudusLatronumEngine {
  constructor() {
    this.reset();
  }
  
  reset() {
    this.board = Array(64).fill(0); // 8x8 board
    // Initialize pieces - 8 per player as per common rules
    // Player 1 at bottom, Player 2 at top
    for (let i = 0; i < 8; i++) {
      this.board[i] = 1; // Player 1 front rank
      this.board[8 + i] = 1; // Player 1 second rank
      this.board[48 + i] = 2; // Player 2 second rank
      this.board[56 + i] = 2; // Player 2 front rank
    }
    this.currentTurn = 1;
    this.selected = null;
    this.winner = null;
    this.statusMessage = "لعبة الجنود الرومانية - دور اللاعب 1";
  }
  
  getLegalMoves(idx) {
    if (this.winner !== null || this.board[idx] !== this.currentTurn) return [];
    const moves = [];
    const row = Math.floor(idx / 8);
    const col = idx % 8;
    // Orthogonal moves by 1
    const dirs = [[-1,0],[1,0],[0,-1],[0,1]];
    for (const [dr, dc] of dirs) {
      const nr = row + dr;
      const nc = col + dc;
      const nidx = nr * 8 + nc;
      if (nr >= 0 && nr < 8 && nc >= 0 && nc < 8 && this.board[nidx] === 0) {
        moves.push(nidx);
      }
    }
    return moves;
  }
  
  clickPoint(idx) {
    if (this.winner !== null) return false;
    if (this.selected === null) {
      if (this.board[idx] === this.currentTurn) {
        this.selected = idx;
        this.statusMessage = "اختر مربعًا للتحرك";
        return true;
      }
      return false;
    }
    const moves = this.getLegalMoves(this.selected);
    if (moves.includes(idx)) {
      // Make move
      this.board[idx] = this.board[this.selected];
      this.board[this.selected] = 0;
      
      // Simple capture check - remove opponent adjacent if surrounded (basic)
      const captures = this.checkCaptures(idx);
      if (captures.length > 0) {
        for (const c of captures) {
          this.board[c] = 0;
        }
      }
      
      // Check win
      const p1Count = this.board.filter(x => x === 1).length;
      const p2Count = this.board.filter(x => x === 2).length;
      if (p1Count === 0) {
        this.winner = 2;
        this.statusMessage = "فاز اللاعب 2!";
      } else if (p2Count === 0) {
        this.winner = 1;
        this.statusMessage = "فاز اللاعب 1!";
      } else {
        this.currentTurn = this.currentTurn === 1 ? 2 : 1;
        this.statusMessage = this.currentTurn === 1 ? "دور اللاعب 1" : "دور اللاعب 2";
      }
      this.selected = null;
      return true;
    } else {
      this.selected = null;
      if (this.board[idx] === this.currentTurn) {
        this.selected = idx;
        this.statusMessage = "اختر مربعًا للتحرك";
      } else {
        this.statusMessage = "حركة غير قانونية";
      }
      return true;
    }
  }
  
  checkCaptures(pos) {
    const captures = [];
    const opp = this.currentTurn === 1 ? 2 : 1;
    const row = Math.floor(pos / 8);
    const col = pos % 8;
    const dirs = [[-1,0],[1,0],[0,-1],[0,1]];
    for (const [dr, dc] of dirs) {
      const nr = row + dr;
      const nc = col + dc;
      const nidx = nr * 8 + nc;
      if (nr >= 0 && nr < 8 && nc >= 0 && nc < 8 && this.board[nidx] === opp) {
        const n2r = nr + dr;
        const n2c = nc + dc;
        const n2idx = n2r * 8 + n2c;
        if (n2r >= 0 && n2r < 8 && n2c >= 0 && n2c < 8 && this.board[n2idx] === this.currentTurn) {
          captures.push(nidx);
        }
      }
    }
    return captures;
  }
  
  makeAiMove() {
    if (this.currentTurn !== 2 || this.winner !== null) return false;
    // Find all my pieces
    const myPieces = [];
    for (let i = 0; i < 64; i++) {
      if (this.board[i] === 2) myPieces.push(i);
    }
    // Try random move
    for (const piece of myPieces) {
      const moves = this.getLegalMoves(piece);
      if (moves.length > 0) {
        this.selected = piece;
        const move = moves[Math.floor(Math.random() * moves.length)];
        this.clickPoint(move);
        return true;
      }
    }
    this.currentTurn = 1;
    this.statusMessage = "لا توجد حركات للاعب 2، دور اللاعب 1";
    return false;
  }
}

// Alias
const LudusLatronum = LudusLatronumEngine;
const LudusLatrunculorumEngine = LudusLatronumEngine;
if (typeof window !== 'undefined') {
  window.UrGameEngine = UrGameEngine;
  window.AlquerqueEngine = AlquerqueEngine;
  window.OwareEngine = OwareEngine;
  window.ShisimaEngine = ShisimaEngine;
  window.SenetEngine = SenetEngine;
  window.MehenEngine = MehenEngine;
  window.NineMensMorrisEngine = NineMensMorrisEngine;
  window.HnefataflEngine = HnefataflEngine;
  window.GoEngine = GoEngine;
  window.ShogiEngine = ShogiEngine;
  window.PachisiEngine = PachisiEngine;
  window.PatolliEngine = PatolliEngine;
  window.PulucEngine = PulucEngine;
  window.FanoronaEngine = FanoronaEngine;
  window.BaoEngine = BaoEngine;
  window.SurakartaEngine = SurakartaEngine;
  window.XiangqiEngine = XiangqiEngine;
  window.ChaturangaEngine = ChaturangaEngine;
  window.YutNoriEngine = YutNoriEngine;
  window.LudusLatronumEngine = LudusLatronumEngine;
  window.LudusLatrunculorumEngine = LudusLatrunculorumEngine;
}
