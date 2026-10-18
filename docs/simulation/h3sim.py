import json, gzip, random, uuid, math, h3
random.seed(42)
import sys
SW=(35.825,128.745); NE=(35.835,128.755); RES=int(sys.argv[1])
def level(c): return 3 if c>=3 else 2 if c>=2 else 1
def wrap(r,msg): return {"isSuccess":True,"code":"COMMON200","message":msg,"result":r}
def size(o):
    b=json.dumps(o,ensure_ascii=False,separators=(',',':')).encode(); return len(b),len(gzip.compress(b))
hot=[(random.uniform(SW[0]+.002,NE[0]-.002),random.uniform(SW[1]+.002,NE[1]-.002)) for _ in range(5)]
def pts(n,mode):
    out=[]
    for _ in range(n):
        if mode=="uniform": out.append((random.uniform(SW[0],NE[0]),random.uniform(SW[1],NE[1])))
        else:
            la,lo=random.choice(hot); s=30/111000  # sigma ~30m
            out.append((random.gauss(la,s),random.gauss(lo,s/math.cos(math.radians(la)))))
    return out
print(f"H3 res {RES} avg cell area m2:", round(h3.average_hexagon_area(RES,'m^2'),1))
print("mode,N,before_raw,before_gz,after_raw,after_gz,cells")
for mode in ["uniform","clustered"]:
    for n in [100,500,1000,2000,4000]:
        p=pts(n,mode); ts=1707137143000
        before=wrap({"timestamp":ts,"visitors":[{"visitorId":str(uuid.uuid4()),"latitude":round(a,6),"longitude":round(b,6)} for a,b in p]},"실시간 위치 조회 성공")
        cnt={}
        for a,b in p: c=h3.latlng_to_cell(a,b,RES); cnt[c]=cnt.get(c,0)+1
        after=wrap({"timestamp":ts,"totalCount":n,"zones":[{"h3Index":c,"count":k,"level":level(k)} for c,k in cnt.items()]},"실시간 혼잡도 조회 성공")
        print(mode,n,*size(before),*size(after),len(cnt),sep=",")
