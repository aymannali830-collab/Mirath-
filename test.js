const S=require('./shisima.js'),assert=require('assert');
const s=S.init();
assert(S.legal(s).length>0);
assert(S.legal(s).every(m=>!s.b[m[1]]&&s.b[m[0]]===1));
assert.strictEqual(S.result({b:[1,0,0,0,1,0,2,2,1],t:2,n:5}),1);
assert.strictEqual(S.result({b:s.b,t:1,n:60}),'draw');
assert.strictEqual(S.result(s),0);
const w={b:[1,1,0,0,1,2,2,2,0],t:1,n:4};
assert.deepStrictEqual(S.ai(w,4),[1,8]);
let g=S.init();while(!S.result(g)){const m=S.ai(g,3);assert(S.legal(g).some(x=>x[0]===m[0]&&x[1]===m[1]));g=S.apply(g,m)}
const O=require('./oware.js'),sm=g=>g.p.reduce((a,b)=>a+b)+g.s[1]+g.s[2];
const o=O.init();
assert.strictEqual(sm(o),48);assert.strictEqual(O.legal(o).length,6);
let c=O.apply({p:[0,0,0,0,0,1,1,0,0,0,0,5],s:[0,0,0],t:1,n:0},5);
assert.strictEqual(c.s[1],2);assert.strictEqual(c.p[6],0);
c=O.apply({p:[0,0,0,0,0,1,1,0,0,0,0,0],s:[0,0,0],t:1,n:0},5);
assert.strictEqual(c.s[1],0);assert.strictEqual(c.p[6],2);
assert.deepStrictEqual(O.legal({p:[0,0,0,0,1,2,0,0,0,0,0,0],s:[0,0,0],t:1,n:0}),[5]);
assert.strictEqual(O.result({p:[3,0,0,0,0,0,0,0,0,0,0,0],s:[0,0,0],t:1,n:0}),1);
let h=O.init();while(!O.result(h)){const m=O.ai(h,2);assert(O.legal(h).includes(m));h=O.apply(h,m);assert.strictEqual(sm(h),48)}
console.log('ok');
