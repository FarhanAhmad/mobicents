//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.5-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.04.25 at 12:01:52 AM WEST 
//


package org.mobicents.slee.sippresence.pojo.rpid;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.mobicents.slee.sippresence.pojo.commonschema.Empty;
import org.mobicents.slee.sippresence.pojo.commonschema.NoteT;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="note" type="{urn:ietf:params:xml:ns:pidf:rpid}Note_t" minOccurs="0"/>
 *         &lt;element name="audio" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="noisy" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
 *                   &lt;element name="ok" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
 *                   &lt;element name="quiet" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
 *                   &lt;element name="unknown" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="video" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="toobright" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
 *                   &lt;element name="ok" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
 *                   &lt;element name="dark" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
 *                   &lt;element name="unknown" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="text" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="uncomfortable" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
 *                   &lt;element name="inappropriate" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
 *                   &lt;element name="ok" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
 *                   &lt;element name="unknown" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:ietf:params:xml:ns:pidf:rpid}fromUntil"/>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "note",
    "audio",
    "video",
    "text"
})
@XmlRootElement(name = "place-is")
public class PlaceIs {

    protected NoteT note;
    protected PlaceIs.Audio audio;
    protected PlaceIs.Video video;
    protected PlaceIs.Text text;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar from;
    @XmlAttribute
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar until;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the note property.
     * 
     * @return
     *     possible object is
     *     {@link NoteT }
     *     
     */
    public NoteT getNote() {
        return note;
    }

    /**
     * Sets the value of the note property.
     * 
     * @param value
     *     allowed object is
     *     {@link NoteT }
     *     
     */
    public void setNote(NoteT value) {
        this.note = value;
    }

    /**
     * Gets the value of the audio property.
     * 
     * @return
     *     possible object is
     *     {@link PlaceIs.Audio }
     *     
     */
    public PlaceIs.Audio getAudio() {
        return audio;
    }

    /**
     * Sets the value of the audio property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlaceIs.Audio }
     *     
     */
    public void setAudio(PlaceIs.Audio value) {
        this.audio = value;
    }

    /**
     * Gets the value of the video property.
     * 
     * @return
     *     possible object is
     *     {@link PlaceIs.Video }
     *     
     */
    public PlaceIs.Video getVideo() {
        return video;
    }

    /**
     * Sets the value of the video property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlaceIs.Video }
     *     
     */
    public void setVideo(PlaceIs.Video value) {
        this.video = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link PlaceIs.Text }
     *     
     */
    public PlaceIs.Text getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlaceIs.Text }
     *     
     */
    public void setText(PlaceIs.Text value) {
        this.text = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the from property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFrom(XMLGregorianCalendar value) {
        this.from = value;
    }

    /**
     * Gets the value of the until property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUntil() {
        return until;
    }

    /**
     * Sets the value of the until property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUntil(XMLGregorianCalendar value) {
        this.until = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="noisy" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
     *         &lt;element name="ok" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
     *         &lt;element name="quiet" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
     *         &lt;element name="unknown" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "noisy",
        "ok",
        "quiet",
        "unknown"
    })
    public static class Audio {

        protected Empty noisy;
        protected Empty ok;
        protected Empty quiet;
        protected Empty unknown;

        /**
         * Gets the value of the noisy property.
         * 
         * @return
         *     possible object is
         *     {@link Empty }
         *     
         */
        public Empty getNoisy() {
            return noisy;
        }

        /**
         * Sets the value of the noisy property.
         * 
         * @param value
         *     allowed object is
         *     {@link Empty }
         *     
         */
        public void setNoisy(Empty value) {
            this.noisy = value;
        }

        /**
         * Gets the value of the ok property.
         * 
         * @return
         *     possible object is
         *     {@link Empty }
         *     
         */
        public Empty getOk() {
            return ok;
        }

        /**
         * Sets the value of the ok property.
         * 
         * @param value
         *     allowed object is
         *     {@link Empty }
         *     
         */
        public void setOk(Empty value) {
            this.ok = value;
        }

        /**
         * Gets the value of the quiet property.
         * 
         * @return
         *     possible object is
         *     {@link Empty }
         *     
         */
        public Empty getQuiet() {
            return quiet;
        }

        /**
         * Sets the value of the quiet property.
         * 
         * @param value
         *     allowed object is
         *     {@link Empty }
         *     
         */
        public void setQuiet(Empty value) {
            this.quiet = value;
        }

        /**
         * Gets the value of the unknown property.
         * 
         * @return
         *     possible object is
         *     {@link Empty }
         *     
         */
        public Empty getUnknown() {
            return unknown;
        }

        /**
         * Sets the value of the unknown property.
         * 
         * @param value
         *     allowed object is
         *     {@link Empty }
         *     
         */
        public void setUnknown(Empty value) {
            this.unknown = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="uncomfortable" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
     *         &lt;element name="inappropriate" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
     *         &lt;element name="ok" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
     *         &lt;element name="unknown" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "uncomfortable",
        "inappropriate",
        "ok",
        "unknown"
    })
    public static class Text {

        protected Empty uncomfortable;
        protected Empty inappropriate;
        protected Empty ok;
        protected Empty unknown;

        /**
         * Gets the value of the uncomfortable property.
         * 
         * @return
         *     possible object is
         *     {@link Empty }
         *     
         */
        public Empty getUncomfortable() {
            return uncomfortable;
        }

        /**
         * Sets the value of the uncomfortable property.
         * 
         * @param value
         *     allowed object is
         *     {@link Empty }
         *     
         */
        public void setUncomfortable(Empty value) {
            this.uncomfortable = value;
        }

        /**
         * Gets the value of the inappropriate property.
         * 
         * @return
         *     possible object is
         *     {@link Empty }
         *     
         */
        public Empty getInappropriate() {
            return inappropriate;
        }

        /**
         * Sets the value of the inappropriate property.
         * 
         * @param value
         *     allowed object is
         *     {@link Empty }
         *     
         */
        public void setInappropriate(Empty value) {
            this.inappropriate = value;
        }

        /**
         * Gets the value of the ok property.
         * 
         * @return
         *     possible object is
         *     {@link Empty }
         *     
         */
        public Empty getOk() {
            return ok;
        }

        /**
         * Sets the value of the ok property.
         * 
         * @param value
         *     allowed object is
         *     {@link Empty }
         *     
         */
        public void setOk(Empty value) {
            this.ok = value;
        }

        /**
         * Gets the value of the unknown property.
         * 
         * @return
         *     possible object is
         *     {@link Empty }
         *     
         */
        public Empty getUnknown() {
            return unknown;
        }

        /**
         * Sets the value of the unknown property.
         * 
         * @param value
         *     allowed object is
         *     {@link Empty }
         *     
         */
        public void setUnknown(Empty value) {
            this.unknown = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="toobright" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
     *         &lt;element name="ok" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
     *         &lt;element name="dark" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
     *         &lt;element name="unknown" type="{urn:ietf:params:xml:ns:pidf:rpid}empty"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "toobright",
        "ok",
        "dark",
        "unknown"
    })
    public static class Video {

        protected Empty toobright;
        protected Empty ok;
        protected Empty dark;
        protected Empty unknown;

        /**
         * Gets the value of the toobright property.
         * 
         * @return
         *     possible object is
         *     {@link Empty }
         *     
         */
        public Empty getToobright() {
            return toobright;
        }

        /**
         * Sets the value of the toobright property.
         * 
         * @param value
         *     allowed object is
         *     {@link Empty }
         *     
         */
        public void setToobright(Empty value) {
            this.toobright = value;
        }

        /**
         * Gets the value of the ok property.
         * 
         * @return
         *     possible object is
         *     {@link Empty }
         *     
         */
        public Empty getOk() {
            return ok;
        }

        /**
         * Sets the value of the ok property.
         * 
         * @param value
         *     allowed object is
         *     {@link Empty }
         *     
         */
        public void setOk(Empty value) {
            this.ok = value;
        }

        /**
         * Gets the value of the dark property.
         * 
         * @return
         *     possible object is
         *     {@link Empty }
         *     
         */
        public Empty getDark() {
            return dark;
        }

        /**
         * Sets the value of the dark property.
         * 
         * @param value
         *     allowed object is
         *     {@link Empty }
         *     
         */
        public void setDark(Empty value) {
            this.dark = value;
        }

        /**
         * Gets the value of the unknown property.
         * 
         * @return
         *     possible object is
         *     {@link Empty }
         *     
         */
        public Empty getUnknown() {
            return unknown;
        }

        /**
         * Sets the value of the unknown property.
         * 
         * @param value
         *     allowed object is
         *     {@link Empty }
         *     
         */
        public void setUnknown(Empty value) {
            this.unknown = value;
        }

    }

}
