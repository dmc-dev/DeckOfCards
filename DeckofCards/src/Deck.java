import java.util.*;

public class Deck {

	public static final int ONE_DECK_MAX_CARDS=52;
	protected static final int BUFSZ = 52;
	
	private int deckCount=0;
	private int startIdx=0;
	private Card[] cards = new Card[BUFSZ];
	
	private static Random random = new Random();
	

	public Deck(boolean deepCopy){
		int idx=0;
		if(deepCopy){
			for(int value=0; value<13; value++)
				for(int suit=0; suit<4; suit++)
					cards[idx]= new Card(suit, value, idx++);
		}else{
			for(int i=0; i<BUFSZ; i++)
				cards[i]=null;
		}
		deckCount=idx;		
	}
	
	
	public Deck(Deck d, boolean deepCopy){
		deckCount=d.deckCount;
		startIdx=d.startIdx;		
		int c=0;
		if(deepCopy){
			for(int i=startIdx; c<d.deckCount ;i=(i+1)%BUFSZ, c++)
				cards[i]=new Card(d.cards[i]);	
		}else{
			System.arraycopy(d.cards, 0, cards, 0, BUFSZ);
		}
	}
	
	public Deck split(boolean deepCopy){
		Deck d = new Deck(this, deepCopy);
		
		int randOffSet = (int)(random.nextDouble() * deckCount);
		int backHalfSz = deckCount - randOffSet;
		int frontHalfSz = deckCount-backHalfSz;
		
		startIdx = (startIdx + randOffSet)%BUFSZ;
					
		deckCount=backHalfSz;		
		d.deckCount=frontHalfSz;
				
		return d;
	}

	public DeckItterator getItterator(){
		return new DeckItterator(cards, startIdx, deckCount);
	}
	
	public Card deal(){
		deckCount--;
		int idx = startIdx;
		startIdx = (startIdx+1)%BUFSZ;
		return cards[idx];
	}
	
	public void swapCartAtIdxWithStart(int idx){
		Card startCard = cards[startIdx];
		cards[startIdx]=cards[idx];
		cards[idx]=startCard;
	}
	
	public Card dealTop(){				
		int idx = calcuateEndIdx();
		deckCount--;
		return cards[idx];
	}
	public Card showTop(){
		return cards[calcuateEndIdx()];
	}
	public Card showTop(int indexDown){
		return cards[calcuateEndIdx()-indexDown];
	}
	public Card show2ndFromTop(){
		return showTop(1);	
	}
	
	public Card dealCardValue(int value){
		int idx = idxOfValue(value);
		if(idx==-1)
			return null;
		
		return getCardAtIdx(idx);
	}
	
	public Card dealRandomCard(){		
		
		int randOffSet = (int)(random.nextDouble() * (deckCount-1));
		int randIdx = (startIdx + randOffSet)%BUFSZ;
		
		return getCardAtIdx(randIdx);
	}
	
	private Card getCardAtIdx(int idx){		
		if(idx==startIdx){
			return deal();
		}
		int endIdx = this.calcuateEndIdx();
		if(idx==endIdx){
			return dealTop();
		}
		Card c = cards[idx]; 
		deckCount--;
		
		if(startIdx<endIdx){
			int lhs = idx-startIdx;
			int rhs = endIdx -idx;
			
			if(lhs>rhs){
				System.arraycopy(cards, idx+1, cards, idx, rhs);//shift to left <==
				
			}else{
				System.arraycopy(cards, startIdx, cards, startIdx+1, lhs);//shift to right ==>
				startIdx = (startIdx+1)%BUFSZ;
			}
		}else{//there is a break in the array 
			if(idx>endIdx){
				int lhs = idx - endIdx;
				System.arraycopy(cards, endIdx, cards, endIdx+1, lhs);//shift to right ==>
				startIdx = (startIdx+1)%BUFSZ;
			}
			else{
				int rhs = endIdx - idx;
				System.arraycopy(cards, idx+1, cards, idx, rhs);//shift to left <==
			}
		}
		return c;		
	}
	
	public Deck splitHalf(boolean deepCopy){
		Deck d = new Deck(this, deepCopy);
		int half = deckCount/2;
		startIdx = (startIdx+half)%BUFSZ;
		deckCount-=half;
				
		d.deckCount-=half;
		return d;
	}
	
	public Deck get(int sz, boolean deepCopy){
		Deck d = new Deck(this, deepCopy);
				
		startIdx = (startIdx+sz)%BUFSZ;
		deckCount-=sz;
				
		d.deckCount=sz;		
		return d;
	}
	
	public void add(Card[] c){
		int idx = (startIdx+deckCount)%BUFSZ;
		for(int i=0; i<c.length; i++){
			cards[idx] = c[i];
			idx = (idx+1)%BUFSZ;
		}
		deckCount+=c.length;
	}
	public void add(Card[] c, int count){
		int idx = (startIdx+deckCount)%BUFSZ;
		for(int i=0; i<count; i++){
			cards[idx] = c[i];
			idx = (idx+1)%BUFSZ;
		}
		deckCount+=count;
	}
	
	public void add(Deck d){
		
		if(startIdx < 0 || startIdx > BUFSZ || deckCount < 0 || deckCount > BUFSZ || d.startIdx < 0 || d.startIdx > BUFSZ || d.deckCount < 0 || d.deckCount > BUFSZ ){
			this.showInfo("ARRRRRAGGG");
			d.showInfo("OSHIT ITS BROK");
		}
		
		int idx_i = (startIdx+deckCount)%BUFSZ;
		int idx_j = d.startIdx;
		
		for(int i=0; i<d.deckCount; i++){
			cards[idx_i] = d.cards[idx_j];
			idx_i = (idx_i+1) % BUFSZ;
			idx_j = (idx_j+1) % BUFSZ;
		}
		deckCount+=d.deckCount;
	}
	
	public void add(Card c){
		cards[(startIdx+deckCount)%BUFSZ]=c;		
		deckCount++;
	}
	
	
	public void clear(){
		deckCount=0;
		startIdx=0;
	}
	
	public void clearAndNull(){
		deckCount=0;
		startIdx=0;
		for(int i=0; i<BUFSZ; i++)
			cards[i]=null;
	}
	
	public void resetDeck(){startIdx=0; deckCount=BUFSZ;}
	public int size(){return deckCount;}
	public boolean full(){return deckCount==BUFSZ;}
	public int freeSpace(){return BUFSZ-deckCount;}
	public int bufferSize(){return BUFSZ;}
	public boolean fullDeck(){return deckCount==52;}
	public boolean hasCards(){return deckCount>0;}
	public boolean lastCard(){return deckCount==1;}
	public boolean finishedCards(){return deckCount==0;}
	public int index(){return startIdx;}
	public int calcuateEndIdx(){return (startIdx+deckCount-1)%BUFSZ;}
	
	public boolean bigger(Deck d){return deckCount>d.deckCount;}
	public boolean less(Deck d){return deckCount<d.deckCount;}
	

	public void Shuffel(){
		int endIdx=this.calcuateEndIdx();
		if(startIdx<endIdx){
			Collections.shuffle(Arrays.asList(cards).subList(startIdx, endIdx));
		}else{
			Collections.shuffle(Arrays.asList(cards).subList(0, endIdx+1));
			Collections.shuffle(Arrays.asList(cards).subList(startIdx, BUFSZ));
		}
	}
	
	public boolean topCardValueIs(int value){return cards[((startIdx+deckCount-1)%BUFSZ)].isValue(value);}
	public boolean snap(){return cards[((startIdx+deckCount-1)%BUFSZ)].isSameValue(cards[((startIdx+deckCount-1)%BUFSZ)-1]);}
	
	public boolean contains(int value, int suit){
		int c=0;
		for(int i=startIdx; c<deckCount; i=(i+1)%BUFSZ, c++)
			if(cards[i].isSameCard(value, suit))
					return true;
		return false;
	}
	public boolean contains(Card card){
		int c=0;
		for(int i=startIdx; c<deckCount; i=(i+1)%BUFSZ, c++)
			if(cards[i].isSameCard(card))
					return true;
		return false;
	}
	
	public int idxOfCard(Card card){
		int c=0;
		for(int i=startIdx; c<deckCount; i=(i+1)%BUFSZ, c++)
			if(cards[i].isSameCard(card))
					return i;
		return -1;
	}
	
	public int idxOfValue(int value){
		int c=0;
		for(int i=startIdx; c<deckCount; i=(i+1)%BUFSZ, c++)
			if(cards[i].isSameValue(value))
					return i;
		return -1;
	}
	
	
	public int countCard(int value, int suit){
		int c=0;
		int total=0;
		for(int i=startIdx; c<deckCount; i=(i+1)%BUFSZ, c++)
			if(cards[i].isSameCard(value, suit))
				total++;
		return total;
	}
	public int countCard(int value){
		int c=0;
		int total=0;
		for(int i=startIdx; c<deckCount; i=(i+1)%BUFSZ, c++)
			if(cards[i].isSameValue(value))
				total++;
		return total;
	}
	public int countSuit(int suit){
		int c=0;
		int total=0;
		for(int i=startIdx; c<deckCount; i=(i+1)%BUFSZ, c++)
			if(cards[i].isSameSuit(suit))
				total++;
		return total;		
	}
	
	
	public String toString(){
		String s = new String();
		int c=0;
		for(int i=startIdx; c<deckCount; i=(i+1)%BUFSZ, c++)			
				s+=cards[i].toString()+" ";
							
		//s+="("+startIdx+", "+calcuateEndIdx()+")="+deckCount;
		return s;
	}
	public String toAbsCardNumbers(){
		String s = new String();
		int c=0;
		int i=startIdx;
		for(; c<deckCount-1; i=(i+1)%BUFSZ, c++)
			s+=cards[i].absCardNumber()+",";
		s+=cards[i].absCardNumber();
		return s;
	}
	
	public byte[] toByteArray(){
		byte[] rtn = new byte[this.deckCount*2];
		int c=0;
		int i=startIdx;
		int targetIdx=0;
		for(; c<deckCount; i=(i+1)%BUFSZ, c++){
			rtn[targetIdx] = cards[i].getValueByte();
			rtn[targetIdx+1] = cards[i].getSuitByte();
			targetIdx+=2;
		}
		return rtn;
	}
	
	public int[] toNmbersArray(){
		int[] rtn = new int[this.deckCount*2];
		int c=0;
		int i=startIdx;
		int targetIdx=0;
		for(; c<deckCount; i=(i+1)%BUFSZ, c++){
			rtn[targetIdx] = cards[i].getValue();
			rtn[targetIdx+1] = cards[i].getSuit();
			targetIdx+=2;
		}
		return rtn;
	}
	
	public void showInfo(String s){		
		System.out.println(s+"===============");
		System.out.println(toString()+"\nidx="+startIdx+" Count="+deckCount+" endIdx ="+((startIdx+deckCount-1)%BUFSZ));
		System.out.println("=================");
	}
	
	public boolean duplicates(){	
		
		int c=0;
		for(int i=startIdx; c<deckCount; i=(i+1)%BUFSZ, c++){
			int count = this.countCard(cards[i].getValue(), cards[i].getSuit());
			if(count>1){
				return true;
			}
		}
		return false;
	}
}
