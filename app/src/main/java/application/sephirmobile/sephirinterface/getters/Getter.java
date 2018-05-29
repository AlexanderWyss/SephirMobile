package application.sephirmobile.sephirinterface.getters;

import application.sephirmobile.sephirinterface.SephirInterface;

public abstract class Getter {
	private SephirInterface sephirInterface;

	public Getter(SephirInterface sephirInterface) {
		this.sephirInterface = sephirInterface;
	}

	public SephirInterface getSephirInterface() {
		return sephirInterface;
	}
}
