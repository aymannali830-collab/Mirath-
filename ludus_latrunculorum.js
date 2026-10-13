(function(R){
function init(){
  const board = Array(64).fill(0);
  for (let i=0; i<8; i++) {
    board[i]=1; board[8+i]=1; board[48+i]=2; board[56+i]=2;
  }
  return {b: board, t: 1, n: 0, sel: null, w: 0};
}
function legalMoves(s, idx){
  if (s.w!==0 || s.b[idx] !== s.t) return [];
  const moves = [];
  const row = Math.floor(idx/8), col = idx%8;
  const dirs=[[ -1,0],[1,0],[0,-1],[0,1]];
  for (const [dr,dc] of dirs){
    const nr=row+dr, nc=col+dc, nidx=nr*8+nc;
    if (nr>=0&&nr<8&&nc>=0&&nc<8&&s.b[nidx]===0) moves.push(nidx);
  }
  return moves;
}
function applyMove(s, fromIdx, toIdx){
  const b = s.b.slice();
  b[toIdx]=b[fromIdx]; b[fromIdx]=0;
  // capture check
  const opp = s.t===1?2:1, me=s.t;
  const row=Math.floor(toIdx/8), col=toIdx%8;
  const dirs=[[ -1,0],[1,0],[0,-1],[0,1]];
  for (const [dr,dc] of dirs){
    const nr=row+dr, nc=col+dc, nidx=nr*8+nc;
    if (nr>=0&&nr<8&&nc>=0&&nc<8&&b[nidx]===opp){
      const n2r=nr+dr, n2c=nc+dc, n2idx=n2r*8+n2c;
      if (n2r>=0&&n2r<8&&n2c>=0&&n2c<8&&b[n2idx]===me){
        b[nidx]=0;
      }
    }
  }
  let p1=0,p2=0;
  for (let i=0;i<64;i++){ if (b[i]===1)p1++; if (b[i]===2)p2++; }
  let w=0;
  if (p1===0) w=2; if (p2===0) w=1;
  return {b, t: w===0 ? (3-s.t):s.t, n:s.n+1, sel:null, w};
}
function result(s){ return s.w; }
function legal(s){ return []; } // basic
function ai(s,d){ return null; }
R.LudusLatronum={init,legal,apply:applyMove,result,ai};
if(typeof module!=='undefined')module.exports=R.LudusLatronum;
})(typeof self!=='undefined'?self:globalThis);
