package com.mk.evaexchange;


import static java.util.Map.entry;

import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
public class ExchangeService {

  record ExchangeRequest(@NotNull Money from, @NotEmpty String to) {

  }

  record ExchangeResponse(Money exchange, Double rate, Money origin) {

  }

  public ExchangeResponse exchange(@Valid ExchangeRequest request) {
    log.debug("Request [{}]",request);
    if(request.from.getCurrency().getCurrencyCode().equals(request.to)){
      return new ExchangeResponse(request.from, 1d, request.from);
    }

    double rate = rateTable.getOrDefault(request.to, 1d);
    log.debug("Rate [{}]", rate);
    Money result = request.from.multiply(rate);
    return new ExchangeResponse(Money.of(result.getNumber(), request.to), rate, request.from);

  }

  private final Map<String, Double> rateTable = Map.ofEntries(
      entry("AED", 3.6676),
      entry("AFN", 89.1648),
      entry("ALL", 111.98028),
      entry("AMD", 390.00414),
      entry("ANG", 1.7857),
      entry("AOA", 507.28809),
      entry("ARS", 164.72202),
      entry("AUD", 1.48356),
      entry("AWG", 1.79833),
      entry("AZN", 1.67371),
      entry("BAM", 1.88231),
      entry("BBD", 1.99886),
      entry("BDT", 106.00464),
      entry("BGN", 1.89137),
      entry("BHD", 0.37583),
      entry("BIF", 2055.26556),
      entry("BMD", 0.99893),
      entry("BND", 1.37474),
      entry("BOB", 6.90517),
      entry("BRL", 5.39334),
      entry("BSD", 1.00099),
      entry("BTN", 81.60477),
      entry("BWP", 12.77057),
      entry("BZD", 2.01328),
      entry("CAD", 1.33505),
      entry("CDF", 1998.98915),
      entry("CHF", 0.94111),
      entry("CLF", 0.02594),
      entry("CLP", 916.2731),
      entry("CNH", 7.14761),
      entry("CNY", 7.15488),
      entry("COP", 4874.97721),
      entry("CUP", 23.99619),
      entry("CVE", 106.34104),
      entry("CZK", 23.38189),
      entry("DJF", 177.83909),
      entry("DKK", 7.14004),
      entry("DOP", 54.48347),
      entry("DZD", 138.83089),
      entry("EGP", 24.54934),
      entry("ERN", 15.12263),
      entry("ETB", 53.42514),
      entry("EUR", 0.95982),
      entry("FJD", 2.23581),
      entry("FKP", 0.82857),
      entry("GBP", 0.82857),
      entry("GEL", 2.66668),
      entry("GHS", 14.04391),
      entry("GIP", 0.82857),
      entry("GMD", 63.23672),
      entry("GNF", 8595.40973),
      entry("GTQ", 7.80162),
      entry("GYD", 209.23026),
      entry("HKD", 7.8167),
      entry("HNL", 24.66526),
      entry("HRK", 7.19919),
      entry("HTG", 139.4432),
      entry("HUF", 393.58025),
      entry("IDR", 15662.19239),
      entry("ILS", 3.42168),
      entry("INR", 81.46466),
      entry("IQD", 1456.85641),
      entry("IRR", 42010.43997),
      entry("ISK", 141.02245),
      entry("JMD", 153.15933),
      entry("JOD", 0.70879),
      entry("JPY", 139.25346),
      entry("KES", 121.83279),
      entry("KGS", 83.29936),
      entry("KHR", 4127.10249),
      entry("KMF", 477.83992),
      entry("KPW", 899.942),
      entry("KRW", 1332.81133),
      entry("KWD", 0.30623),
      entry("KYD", 0.81881),
      entry("KZT", 464.04425),
      entry("LAK", 17035.62847),
      entry("LBP", 1508.3354),
      entry("LKR", 361.82451),
      entry("LRD", 153.90753),
      entry("LSL", 16.96735),
      entry("LYD", 4.89543),
      entry("MAD", 10.66658),
      entry("MDL", 19.18013),
      entry("MGA", 4333.76419),
      entry("MKD", 59.4659),
      entry("MMK", 2093.58688),
      entry("MNT", 3418.46052),
      entry("MOP", 7.92849),
      entry("MRU", 38.06827),
      entry("MUR", 43.97307),
      entry("MVR", 15.65142),
      entry("MWK", 1023.21651),
      entry("MXN", 19.35256),
      entry("MYR", 4.57173),
      entry("MZN", 63.65026),
      entry("NAD", 16.90587),
      entry("NGN", 443.04665),
      entry("NOK", 9.94051),
      entry("NPR", 128.85575),
      entry("NZD", 1.60049),
      entry("OMR", 0.38292),
      entry("PAB", 0.99893),
      entry("PEN", 3.84288),
      entry("PGK", 3.46624),
      entry("PHP", 56.6907),
      entry("PKR", 223.19331),
      entry("PLN", 4.51058),
      entry("PYG", 7198.76543),
      entry("QAR", 3.63835),
      entry("RON", 4.73872),
      entry("RSD", 112.52962),
      entry("RUB", 60.18784),
      entry("RWF", 1053.35156),
      entry("SAR", 3.74858),
      entry("SCR", 14.37294),
      entry("SDG", 564.99876),
      entry("SEK", 10.43574),
      entry("SGD", 1.3765),
      entry("SHP", 0.82857),
      entry("SLL", 18160.90811),
      entry("SOS", 568.54669),
      entry("SRD", 30.74787),
      entry("SYP", 2511.39282),
      entry("SZL", 16.95609),
      entry("THB", 36.01715),
      entry("TJS", 10.04068),
      entry("TMT", 3.49529),
      entry("TND", 3.23499),
      entry("TOP", 2.34778),
      entry("TRY", 18.59334),
      entry("TTD", 6.76511),
      entry("TWD", 31.10117),
      entry("TZS", 2329.72077),
      entry("UAH", 36.68084),
      entry("UGX", 3741.42017),
      entry("USD", 1.0),
      entry("UYU", 39.30326),
      entry("UZS", 11188.16803),
      entry("VND", 24826.41478),
      entry("VUV", 119.74729),
      entry("WST", 2.69194),
      entry("XAF", 629.78291),
      entry("XCD", 2.6989),
      entry("XDR", 0.75972),
      entry("XOF", 629.78291),
      entry("XPF", 114.48339),
      entry("YER", 249.64869),
      entry("ZAR", 16.96909),
      entry("ZMW", 16.83371)
  );

}
