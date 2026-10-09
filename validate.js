const fs=require('fs');global.window=global;require('./data.js');
const G=CATALOG.games,ids=new Set(),E=[];
G.forEach(g=>{
 if(ids.has(g.id))E.push('duplicate id '+g.id);ids.add(g.id);
 ['id','title','titleEn','country','region','category','rules'].forEach(k=>{if(!g[k])E.push(g.id+': missing '+k)});
 if(!(g.players>=1))E.push(g.id+': invalid players');
 const p=g.playability;
 if(!p||!['planned','playable'].includes(p.status))E.push(g.id+': invalid playability');
 else if(p.status==='playable'&&!fs.existsSync(p.engine+'.js'))E.push(g.id+': engine file missing');
 if(g.review==='approved'&&!g.sources.length)E.push(g.id+': approved without sources');
});
if(E.length){console.error(E.join('\n'));process.exit(1)}
console.log('catalog ok ('+G.length+' games)');
