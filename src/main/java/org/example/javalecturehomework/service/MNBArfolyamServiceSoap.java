
package org.example.javalecturehomework.service;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 3.0.0
 * Generated source version: 3.0
 * 
 */
@WebService(name = "MNBArfolyamServiceSoap", targetNamespace = "http://www.mnb.hu/webservices/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface MNBArfolyamServiceSoap {


    /**
     * 
     * @return
     *     returns java.lang.String
     * @throws MNBArfolyamServiceSoapGetCurrenciesStringFaultFaultMessage
     */
    @WebMethod(operationName = "GetCurrencies", action = "http://www.mnb.hu/webservices/MNBArfolyamServiceSoap/GetCurrencies")
    @WebResult(name = "GetCurrenciesResult", targetNamespace = "http://www.mnb.hu/webservices/")
    @RequestWrapper(localName = "GetCurrencies", targetNamespace = "http://www.mnb.hu/webservices/", className = "org.example.javalecturehomework.service.GetCurrenciesRequestBody")
    @ResponseWrapper(localName = "GetCurrenciesResponse", targetNamespace = "http://www.mnb.hu/webservices/", className = "org.example.javalecturehomework.service.GetCurrenciesResponseBody")
    public String getCurrencies()
        throws MNBArfolyamServiceSoapGetCurrenciesStringFaultFaultMessage
    ;

    /**
     * 
     * @param currencyNames
     * @return
     *     returns java.lang.String
     * @throws MNBArfolyamServiceSoapGetCurrencyUnitsStringFaultFaultMessage
     */
    @WebMethod(operationName = "GetCurrencyUnits", action = "http://www.mnb.hu/webservices/MNBArfolyamServiceSoap/GetCurrencyUnits")
    @WebResult(name = "GetCurrencyUnitsResult", targetNamespace = "http://www.mnb.hu/webservices/")
    @RequestWrapper(localName = "GetCurrencyUnits", targetNamespace = "http://www.mnb.hu/webservices/", className = "org.example.javalecturehomework.service.GetCurrencyUnitsRequestBody")
    @ResponseWrapper(localName = "GetCurrencyUnitsResponse", targetNamespace = "http://www.mnb.hu/webservices/", className = "org.example.javalecturehomework.service.GetCurrencyUnitsResponseBody")
    public String getCurrencyUnits(
        @WebParam(name = "currencyNames", targetNamespace = "http://www.mnb.hu/webservices/")
        String currencyNames)
        throws MNBArfolyamServiceSoapGetCurrencyUnitsStringFaultFaultMessage
    ;

    /**
     * 
     * @return
     *     returns java.lang.String
     * @throws MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage
     */
    @WebMethod(operationName = "GetCurrentExchangeRates", action = "http://www.mnb.hu/webservices/MNBArfolyamServiceSoap/GetCurrentExchangeRates")
    @WebResult(name = "GetCurrentExchangeRatesResult", targetNamespace = "http://www.mnb.hu/webservices/")
    @RequestWrapper(localName = "GetCurrentExchangeRates", targetNamespace = "http://www.mnb.hu/webservices/", className = "org.example.javalecturehomework.service.GetCurrentExchangeRatesRequestBody")
    @ResponseWrapper(localName = "GetCurrentExchangeRatesResponse", targetNamespace = "http://www.mnb.hu/webservices/", className = "org.example.javalecturehomework.service.GetCurrentExchangeRatesResponseBody")
    public String getCurrentExchangeRates()
        throws MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage
    ;

    /**
     * 
     * @return
     *     returns java.lang.String
     * @throws MNBArfolyamServiceSoapGetDateIntervalStringFaultFaultMessage
     */
    @WebMethod(operationName = "GetDateInterval", action = "http://www.mnb.hu/webservices/MNBArfolyamServiceSoap/GetDateInterval")
    @WebResult(name = "GetDateIntervalResult", targetNamespace = "http://www.mnb.hu/webservices/")
    @RequestWrapper(localName = "GetDateInterval", targetNamespace = "http://www.mnb.hu/webservices/", className = "org.example.javalecturehomework.service.GetDateIntervalRequestBody")
    @ResponseWrapper(localName = "GetDateIntervalResponse", targetNamespace = "http://www.mnb.hu/webservices/", className = "org.example.javalecturehomework.service.GetDateIntervalResponseBody")
    public String getDateInterval()
        throws MNBArfolyamServiceSoapGetDateIntervalStringFaultFaultMessage
    ;

    /**
     * 
     * @param currencyNames
     * @param endDate
     * @param startDate
     * @return
     *     returns java.lang.String
     * @throws MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage
     */
    @WebMethod(operationName = "GetExchangeRates", action = "http://www.mnb.hu/webservices/MNBArfolyamServiceSoap/GetExchangeRates")
    @WebResult(name = "GetExchangeRatesResult", targetNamespace = "http://www.mnb.hu/webservices/")
    @RequestWrapper(localName = "GetExchangeRates", targetNamespace = "http://www.mnb.hu/webservices/", className = "org.example.javalecturehomework.service.GetExchangeRatesRequestBody")
    @ResponseWrapper(localName = "GetExchangeRatesResponse", targetNamespace = "http://www.mnb.hu/webservices/", className = "org.example.javalecturehomework.service.GetExchangeRatesResponseBody")
    public String getExchangeRates(
        @WebParam(name = "startDate", targetNamespace = "http://www.mnb.hu/webservices/")
        String startDate,
        @WebParam(name = "endDate", targetNamespace = "http://www.mnb.hu/webservices/")
        String endDate,
        @WebParam(name = "currencyNames", targetNamespace = "http://www.mnb.hu/webservices/")
        String currencyNames)
        throws MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage
    ;

    /**
     * 
     * @return
     *     returns java.lang.String
     * @throws MNBArfolyamServiceSoapGetInfoStringFaultFaultMessage
     */
    @WebMethod(operationName = "GetInfo", action = "http://www.mnb.hu/webservices/MNBArfolyamServiceSoap/GetInfo")
    @WebResult(name = "GetInfoResult", targetNamespace = "http://www.mnb.hu/webservices/")
    @RequestWrapper(localName = "GetInfo", targetNamespace = "http://www.mnb.hu/webservices/", className = "org.example.javalecturehomework.service.GetInfoRequestBody")
    @ResponseWrapper(localName = "GetInfoResponse", targetNamespace = "http://www.mnb.hu/webservices/", className = "org.example.javalecturehomework.service.GetInfoResponseBody")
    public String getInfo()
        throws MNBArfolyamServiceSoapGetInfoStringFaultFaultMessage
    ;

}
