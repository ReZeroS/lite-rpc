package lite.summer.service.v6;


import lite.summer.stereotype.Component;
import lite.summer.util.MessageTracker;

@Component(value="petStore")
public class PetStoreService implements IPetStoreService {
	
	public PetStoreService() {		
		
	}
	
	public void placeOrder(){
		System.out.println("place order");
		MessageTracker.addMsg("place order");
	}
	
	
	
}
