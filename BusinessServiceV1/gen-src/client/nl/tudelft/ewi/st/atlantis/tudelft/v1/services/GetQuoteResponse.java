
package nl.tudelft.ewi.st.atlantis.tudelft.v1.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import nl.tudelft.ewi.st.atlantis.tudelft.v1.types.QuoteData;
import org.ebayopensource.turmeric.common.v1.types.BaseResponse;


/**
 * 
 * 						Document goes here
 * 					
 * 
 * <p>Java class for GetQuoteResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetQuoteResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ebayopensource.org/turmeric/common/v1/types}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="quoteData" type="{http://atlantis.st.ewi.tudelft.nl/tudelft/v1/types}QuoteData"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetQuoteResponse", propOrder = {
    "quoteData"
})
public class GetQuoteResponse
    extends BaseResponse
{

    @XmlElement(required = true)
    protected QuoteData quoteData;

    /**
     * Gets the value of the quoteData property.
     * 
     * @return
     *     possible object is
     *     {@link QuoteData }
     *     
     */
    public QuoteData getQuoteData() {
        return quoteData;
    }

    /**
     * Sets the value of the quoteData property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuoteData }
     *     
     */
    public void setQuoteData(QuoteData value) {
        this.quoteData = value;
    }

}
