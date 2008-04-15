/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.servlet.sip;
/**
 * Factory interface for a variety of SIP Servlet API abstractions.
 * SIP servlet containers are requried to make a SipFactory instance available to applications through a ServletContext attribute with name javax.servlet.sip.SipFactory.
 */
public interface SipFactory{
    /**
     * Returns a Address corresponding to the specified string. The resulting object can be used, for example, as the value of From or To headers of locally initiated SIP requests.
     * The special argument "*" results in a wildcard Address being returned, that is, an Address for which isWildcard returns true. Such addresses are for use in Contact headers only.
     * The specified address string must be UTF-8 encoded. Furthermore, if the URI component of the address string contains any reserved characters then they must be escaped according to RFC2396 as indicated for createURI(String)
     */
    javax.servlet.sip.Address createAddress(java.lang.String addr) throws javax.servlet.sip.ServletParseException;

    /**
     * Returns an Address with the specified URI and no display name.
     */
    javax.servlet.sip.Address createAddress(javax.servlet.sip.URI uri);

    /**
     * Returns a new Address with the specified URI and display name.
     */
    javax.servlet.sip.Address createAddress(javax.servlet.sip.URI uri, java.lang.String displayName);

    /**
     * Returns a new SipApplicationSession. This is useful, for example, when an application is being initialized and wishes to perform some signaling action.
     */
    javax.servlet.sip.SipApplicationSession createApplicationSession();

    /**
     * Creates a new Parameterable parsed from the specified string. The string must be in the following format: field-name: field-value *(;parameter-name=parameter-value)
     */
    javax.servlet.sip.Parameterable createParameterable(java.lang.String s);

    /**
     * Returns a new request object with the specified request method, From, and To headers. The returned request object exists in a new SipSession which belongs to the specified SipApplicationSession.
     * This method is used by servlets acting as SIP clients in order to send a request in a new call leg. The container is responsible for assigning the request appropriate Call-ID and CSeq headers, as well as Contact header if the method is not REGISTER.
     * This method makes a copy of the from and to arguments and associates them with the new SipSession. Any component of the from and to URIs not allowed in the context of SIP From and To headers are removed from the copies. This includes, headers and various parameters. Also, a "tag" parameter in either of the copied from or to is also removed, as it is illegal in an initial To header and the container will choose it's own tag for the From header. The copied from and to addresses can be obtained from the SipSession but must not be modified by applications.
     */
    javax.servlet.sip.SipServletRequest createRequest(javax.servlet.sip.SipApplicationSession appSession, java.lang.String method, javax.servlet.sip.Address from, javax.servlet.sip.Address to);

    /**
     * Returns a new request object with the specified request method, From, and To headers. The returned request object exists in a new SipSession which belongs to the specified SipApplicationSession.
     * This method is used by servlets acting as SIP clients in order to send a request in a new call leg. The container is responsible for assigning the request appropriate Call-ID and CSeq headers, as well as Contact header if the method is not REGISTER.
     * This method is functionally equivalent to: createRequest(method, f.createAddress(from), f.createAddress(to)); Note that this implies that if either of the from or to argument is a SIP URI containing parameters, the URI must be enclosed in angle brackets. Otherwise the address will be parsed as if the parameter belongs to the address and not the URI.
     */
    javax.servlet.sip.SipServletRequest createRequest(javax.servlet.sip.SipApplicationSession appSession, java.lang.String method, java.lang.String from, java.lang.String to) throws javax.servlet.sip.ServletParseException;

    /**
     * Returns a new request object with the specified request method, From, and To headers. The returned request object exists in a new SipSession which belongs to the specified SipApplicationSession.
     * This method is used by servlets acting as SIP clients in order to send a request in a new call leg. The container is responsible for assigning the request appropriate Call-ID and CSeq headers, as well as Contact header if the method is not REGISTER.
     * This method makes a copy of the from and to arguments and associates them with the new SipSession. Any component of the from and to URIs not allowed in the context of SIP From and To headers are removed from the copies. This includes, headers and various parameters. The from and to addresses can subsequently be obtained from the SipSession or the returned request object but must not be modified by applications.
     */
    javax.servlet.sip.SipServletRequest createRequest(javax.servlet.sip.SipApplicationSession appSession, java.lang.String method, javax.servlet.sip.URI from, javax.servlet.sip.URI to);

    /**
     * Deprecated.
     * Creates a new request object belonging to a new SipSession. The new request is similar to the specified origRequest in that the method and the majority of header fields are copied from origRequest to the new request. The SipSession created for the new request also shares the same SipApplicationSession associated with the original request.
     * This method satisfies the following rules: The From header field of the new request has a new tag chosen by the container. The To header field of the new request has no tag. If the sameCallId argument is false, the new request (and the corresponding SipSession)is assigned a new Call-ID. Record-Route and Via header fields are not copied. As usual, the container will add its own Via header field to the request when it's actually sent outside the application server. For non-REGISTER requests, the Contact header field is not copied but is populated by the container as usual.
     * This method provides a convenient and efficient way of constructing the second "leg" of a B2BUA application. It is used only for the initial request. Subsequent requests in either leg must be created using SipSession.createRequest(java.lang.String) as usual.
     */
    javax.servlet.sip.SipServletRequest createRequest(javax.servlet.sip.SipServletRequest origRequest, boolean sameCallId);

    /**
     * Constructs a SipURI with the specified user and host components. The scheme will initially be sip but the application may change it to sips by calling setSecure(true) on the returned SipURI. Likewise, the port number of the new URI is left unspecified but may subsequently be set by calling setPort on the returned SipURI.
     * If the specified URI string contains any reserved characters, then they must be escaped according to RFC2396. For example, the URI string representing sip:first:last@host.net must be encoded as sip:first%3last@host.net.
     */
    javax.servlet.sip.SipURI createSipURI(java.lang.String user, java.lang.String host);

    /**
     * Returns a URI object corresponding to the specified string, which should represent an escaped SIP, SIPS, or tel URI. The URI may then be used as request URI in SIP requests or as the URI component of
     * objects.
     * Implementations must be able to represent URIs of any scheme. This method returns a SipURI object if the specified string is a sip or a sips URI, and a TelURL object if it's a tel URL.
     * If the specified URI string contains any reserved characters, then they must be escaped according to RFC2396. For example, the URI string representing sip:first:last@host.net must be encoded as sip:first%3last@host.net.
     */
    javax.servlet.sip.URI createURI(java.lang.String uri) throws javax.servlet.sip.ServletParseException;

}
