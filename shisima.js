(function(R){
var A=[],L=[0,1,2,3].map(function(i){return[i,8,i+4]});
for(var i=0;i<8;i++)A[i]=[(i+1)%8,(i+7)%8,8];A[8]=[0,1,2,3,4,5,6,7];
function init(){return{b:[1,1,1,0,2,2,2,0,0],t:1,n:0}}
function legal(s){var r=[];for(var f=0;f<9;f++)if(s.b[f]===s.t)A[f].forEach(function(to){if(!s.b[to])r.push([f,to])});return r}
function apply(s,m){var b=s.b.slice();b[m[1]]=b[m[0]];b[m[0]]=0;return{b:b,t:3-s.t,n:s.n+1}}
function result(s){for(var k=0;k<4;k++){var l=L[k],p=s.b[l[0]];if(p&&p===s.b[l[1]]&&p===s.b[l[2]])return p}
return(s.n>=60||!legal(s).length)?'draw':0}
function mm(s,d,a,b,me){var w=result(s);if(w)return w===me?100+d:w==='draw'?0:-100-d;
if(!d)return s.b[8]===me?1:s.b[8]?-1:0;var ms=legal(s),best,v,j;
if(s.t===me){best=-1e9;for(j=0;j<ms.length;j++){v=mm(apply(s,ms[j]),d-1,a,b,me);if(v>best)best=v;if(best>a)a=best;if(a>=b)break}}
else{best=1e9;for(j=0;j<ms.length;j++){v=mm(apply(s,ms[j]),d-1,a,b,me);if(v<best)best=v;if(best<b)b=best;if(a>=b)break}}
return best}
function ai(s,d){var me=s.t,bv=-1e9,bm=null;legal(s).forEach(function(m){var v=mm(apply(s,m),(d||6)-1,-1e9,1e9,me);if(v>bv){bv=v;bm=m}});return bm}
R.Shisima={init:init,legal:legal,apply:apply,result:result,ai:ai,A:A};
if(typeof module!=='undefined')module.exports=R.Shisima;
})(typeof self!=='undefined'?self:globalThis);
