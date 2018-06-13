package application.sephirmobile.sephirinterface.entitys;

import java.io.Serializable;

public class Certification implements Serializable {
  private final String cfid;
  private final String cftoken;

  public Certification(String cfid, String cftoken) {
    this.cfid = cfid;
    this.cftoken = cftoken;
  }

  public String getCfid() {
    return cfid;
  }

  public String getCftoken() {
    return cftoken;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cfid == null) ? 0 : cfid.hashCode());
    result = prime * result + ((cftoken == null) ? 0 : cftoken.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Certification other = (Certification) obj;
    if (cfid == null) {
      if (other.cfid != null)
        return false;
    } else if (!cfid.equals(other.cfid))
      return false;
    if (cftoken == null) {
      if (other.cftoken != null)
        return false;
    } else if (!cftoken.equals(other.cftoken))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Certification [cfid=" + cfid + ", cftoken=" + cftoken + "]";
  }
}
