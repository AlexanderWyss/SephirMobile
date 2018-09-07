package application.sephirmobile.sephirinterface.getters;

import application.sephirmobile.sephirinterface.SephirInterface;

public abstract class Getter {
	private final SephirInterface sephirInterface;

	protected Getter(SephirInterface sephirInterface) {
		this.sephirInterface = sephirInterface;
	}

	protected SephirInterface getSephirInterface() {
		return sephirInterface;
	}
}
