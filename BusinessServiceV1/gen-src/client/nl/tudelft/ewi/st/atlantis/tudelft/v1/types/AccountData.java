
package nl.tudelft.ewi.st.atlantis.tudelft.v1.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for AccountData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="profileID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="openBalance" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="balance" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="creationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="lastLogin" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="logoutCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="loginCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="accountID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountData", propOrder = {
    "profileID",
    "openBalance",
    "balance",
    "creationDate",
    "lastLogin",
    "logoutCount",
    "loginCount",
    "accountID"
})
public class AccountData {

    @XmlElement(required = true)
    protected String profileID;
    protected double openBalance;
    protected double balance;
    @XmlElement(required = true)
    protected XMLGregorianCalendar creationDate;
    @XmlElement(required = true)
    protected XMLGregorianCalendar lastLogin;
    protected int logoutCount;
    protected int loginCount;
    protected int accountID;

    /**
     * Gets the value of the profileID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfileID() {
        return profileID;
    }

    /**
     * Sets the value of the profileID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfileID(String value) {
        this.profileID = value;
    }

    /**
     * Gets the value of the openBalance property.
     * 
     */
    public double getOpenBalance() {
        return openBalance;
    }

    /**
     * Sets the value of the openBalance property.
     * 
     */
    public void setOpenBalance(double value) {
        this.openBalance = value;
    }

    /**
     * Gets the value of the balance property.
     * 
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the value of the balance property.
     * 
     */
    public void setBalance(double value) {
        this.balance = value;
    }

    /**
     * Gets the value of the creationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the value of the creationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreationDate(XMLGregorianCalendar value) {
        this.creationDate = value;
    }

    /**
     * Gets the value of the lastLogin property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastLogin() {
        return lastLogin;
    }

    /**
     * Sets the value of the lastLogin property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastLogin(XMLGregorianCalendar value) {
        this.lastLogin = value;
    }

    /**
     * Gets the value of the logoutCount property.
     * 
     */
    public int getLogoutCount() {
        return logoutCount;
    }

    /**
     * Sets the value of the logoutCount property.
     * 
     */
    public void setLogoutCount(int value) {
        this.logoutCount = value;
    }

    /**
     * Gets the value of the loginCount property.
     * 
     */
    public int getLoginCount() {
        return loginCount;
    }

    /**
     * Sets the value of the loginCount property.
     * 
     */
    public void setLoginCount(int value) {
        this.loginCount = value;
    }

    /**
     * Gets the value of the accountID property.
     * 
     */
    public int getAccountID() {
        return accountID;
    }

    /**
     * Sets the value of the accountID property.
     * 
     */
    public void setAccountID(int value) {
        this.accountID = value;
    }

}
