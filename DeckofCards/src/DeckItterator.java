
public class DeckItterator {

	private Card[] cards;
	private int startIdx;
	private int deckCount;
	private int count=0;
	
	
	DeckItterator(Card[] cards, int startIdx, int deckCount){
		this.cards = cards;
		this.startIdx = startIdx;
		this.deckCount = deckCount;
	}
	
	boolean hasMore(){
		return count<deckCount; 
	}
	
	Card getNext(){
		Card c = cards[startIdx];
		startIdx=(startIdx+1)% Deck.BUFSZ; 
		count++;
		return c;
	} 
	
}
