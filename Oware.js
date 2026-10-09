(function(R){
function own(i){return i<6?1:2}
function sum(p,t){var x=0,i=t===1?0:6;for(;i<(t===1?6:12);i++)x+=p[i];return x}
function init(){return{p:[4,4,4,4,4,4,4,4,4,4,4,4],s:[0,0,0],t:1,n:0}}
function sow(p,i){var q=p.slice(),c=q[i],j=i;q[i]=0;while(c>0){j=(j+1)%12;if(j===i)continue;q[j]++;c--}return{p:q,last:j}}
function legal(s){var o=3-s.t,r=[],a=s.t===1?0:6,i;
for(i=a;i<a+6;i++)if(s.p[i]>0&&(sum(s.p,o)>0||sum(sow(s.p,i).p,o)>0))r.push(i);return r}
function apply(s,i){var r=sow(s.p,i),q=r.p,k=r.last,o=3-s.t,cap=[],tot=0,sc=s.s.slice();
while(own(k)===o&&(q[k]===2||q[k]===3)){cap.push(k);tot+=q[k];k=(k+11)%12}
if(cap.length&&tot<sum(q,o)){cap.forEach(function(x){q[x]=0});sc[s.t]+=tot}
return{p:q,s:sc,t:o,n:s.n+1}}
function fin(s){var a=s.s[1]+sum(s.p,1),b=s.s[2]+sum(s.p,2);return a>b?1:b>a?2:'draw'}
function result(s){if(s.s[1]>24)return 1;if(s.s[2]>24)return 2;if(s.s[1]===24&&s.s[2]===24)return'draw';
return(s.n>=200||!legal(s).length)?fin(s):0}
function ev(s,me){return 10*(s.s[me]-s.s[3-me])+sum(s.p,me)-sum(s.p,3-me)}
function mm(s,d,a,b,me){var w=result(s);if(w)return w===me?1000+d:w==='draw'?0:-1000-d;
if(!d)return ev(s,me);var ms=legal(s),best,v,j;
if(s.t===me){best=-1e9;for(j=0;j<ms.length;j++){v=mm(apply(s,ms[j]),d-1,a,b,me);if(v>best)best=v;if(best>a)a=best;if(a>=b)break}}
else{best=1e9;for(j=0;j<ms.length;j++){v=mm(apply(s,ms[j]),d-1,a,b,me);if(v<best)best=v;if(best<b)b=best;if(a>=b)break}}
return best}
function ai(s,d){var me=s.t,bv=-1e9,bm=null;legal(s).forEach(function(m){var v=mm(apply(s,m),(d||6)-1,-1e9,1e9,me);if(v>bv){bv=v;bm=m}});return bm}
R.Oware={init:init,legal:legal,apply:apply,result:result,ai:ai};
if(typeof module!=='undefined')module.exports=R.Oware;
})(typeof self!=='undefined'?self:globalThis);
