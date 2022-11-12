package io.github.rezeros.service.v6;


import io.github.rezeros.stereotype.Component;
import io.github.rezeros.util.MessageTracker;

@Component(value="petStore")
public class PetStoreService implements IPetStoreService {
	
	public PetStoreService() {		
		
	}
	
	public void placeOrder(){
		System.out.println("place order");
		MessageTracker.addMsg("place order");
	}
	
	
	
}
