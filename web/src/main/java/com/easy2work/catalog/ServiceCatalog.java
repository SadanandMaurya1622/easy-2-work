package com.easy2work.catalog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class ServiceCatalog {

    private static final List<String> VISIT_FLOW = List.of(
            "Aap website par book karte hain — address + short note; aapko reference milta hai.",
            "Verified partner job accept karta hai aur zarurat ho to call/WhatsApp se time confirm karta hai.",
            "Ghar par pahunch kar pehle scope samjhaya jata hai; aapki haan ke baad kaam shuru.",
            "Payment booking ke hisaab se (app / UPI / cash) — detail call par clear ho jati hai."
    );

    private static final Map<String, ServiceDetail> BY_CODE = Map.ofEntries(
            Map.entry("ELECTRICAL", new ServiceDetail(
                    "ELECTRICAL",
                    "Electrical repair",
                    "https://images.unsplash.com/photo-1621905252507-b35492cc74b4?w=1200&q=85",
                    "Ghar par safe electrical check aur repair — chhota kaam ho ya wiring/switch/MCB jaisi common problems. "
                            + "Partner standard tools ke saath aata hai; pehle safety, phir kaam.",
                    "From ₹199",
                    "Visit + basic diagnosis. Spare parts, concealed wiring & heavy rework billed separately after on-site estimate.",
                    List.of(
                            "Fault finding: switch, socket, loose wiring, tripping MCB (basic level)",
                            "Tube-light, bulb holder, fan connection / basic fitting (scope ke hisaab se)",
                            "Temporary safe isolation / advice agar major rework ya new wiring chahiye",
                            "Kaam se pehle short estimate / scope — aap approve karein tabhi aage"
                    ),
                    List.of(
                            "Main power access / MCB board tak pahunch (safe, clutter-free)",
                            "Stool/ladder agar high point hai — ya humein batayein taaki sahi partner aaye",
                            "Naye heavy appliances ke liye separate power point / earthing — pehle batayein",
                            "Koi bhi purani illegal wiring dikh jaye to usko theek karwana zaroori ho sakta hai"
                    ),
                    List.of(
                            "Full home rewiring, concealed conduit, heavy industrial panels",
                            "Company warranty wale branded appliances ke andar ki repair (OEM service)",
                            "Material ka bill alag ho sakta hai — jo parts lagenge woh on-site / estimate par",
                            "Emergency fire / shock — turant local electricity board / 108 bhi call karein"
                    ),
                    VISIT_FLOW
            )),
            Map.entry("AC", new ServiceDetail(
                    "AC",
                    "AC servicing",
                    "https://images.unsplash.com/photo-1631545914464-f152c32b1b2c?w=1200&q=85",
                    "AC ki cooling weak hai, smell aa rahi hai, ya season se pehle service chahiye? "
                            + "Filter, coil cleaning, drain check aur basic performance test — ghar par.",
                    "From ₹449",
                    "Per split AC standard servicing. Gas top-up, PCB repair & spare parts charged as per actual use.",
                    List.of(
                            "Indoor unit: filter wash, visible coil & blower area cleaning (scope ke hisaab se)",
                            "Outdoor unit: dust cleanup, fins check jahan safely reachable ho",
                            "Drain pipe flush / clog basic check; water leakage common points",
                            "Cooling test run; agar gas / compressor doubt ho to clear next-step advice"
                    ),
                    List.of(
                            "AC ke liye safe access — balcony / window / service platform",
                            "Power supply ON; remote available ho",
                            "Paani ki line / drain outlet block na ho — agar issue ho to batayein",
                            "Heavy gas refill ya compressor job ke liye alag visit / rate ho sakta hai"
                    ),
                    List.of(
                            "Full gas top-up quantity guarantee bina leak check ke",
                            "PCB / inverter board level electronic repair (specialist job)",
                            "Scaffolding / rope access for unsafe outdoor — woh arrange karna pad sakta hai",
                            "Spare parts (gas, capacitor, etc.) — actual use ke hisaab se charge"
                    ),
                    VISIT_FLOW
            )),
            Map.entry("COOLER", new ServiceDetail(
                    "COOLER",
                    "Cooler repair",
                    "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=1200&q=85",
                    "Desert cooler thanda nahi kar raha, pump ya fan kharab, ya paani overflow? "
                            + "Ghar par basic repair aur seasonal tune-up.",
                    "From ₹249",
                    "Basic repair & seasonal tune-up. New motor, pads or major body work quoted after inspection.",
                    List.of(
                            "Pump, motor, fan belt / coupling basic check & repair",
                            "Cooling pads condition check; replacement guidance + fitting if arranged",
                            "Water distribution pipe, float valve, overflow issues (reachable parts)",
                            "Season open / close basic service — cleaning tips ke saath"
                    ),
                    List.of(
                            "Cooler ke paas power socket & paani ki supply",
                            "Unit accessible ho — chhota space ho to batayein",
                            "Naye pads / motor agar replace ho to cost parts ke hisaab se alag",
                            "Bada body crack / tank leak — welding/shop job ho sakta hai"
                    ),
                    List.of(
                            "Complete motor rewinding in workshop",
                            "Plastic body major crack permanent weld",
                            "Brand-specific spare agar market mein na mile — delay possible"
                    ),
                    VISIT_FLOW
            )),
            Map.entry("LAUNDRY", new ServiceDetail(
                    "LAUNDRY",
                    "Laundry help",
                    "https://images.unsplash.com/photo-1582735689369-4fe89db7114c?w=1200&q=85",
                    "Ghar par washing + drying + ironing help — busy week ya mehmaan ke baad extra haath. "
                            + "Load size aur time pehle clear karte hain.",
                    "From ₹299",
                    "Per agreed session (load & duration). Extra load, dry-clean only items or special care may cost more.",
                    List.of(
                            "Machine wash ya bucket wash — jo booking mein agree ho",
                            "Drying (rack/sun/limited spin) aur folding",
                            "Daily wear ironing — count / duration booking ke hisaab se",
                            "Care label respect — delicate kapde agar aap alag bata dein"
                    ),
                    List.of(
                            "Detergent, bucket, drying space / clips — basic arrangement aapke yahan",
                            "Machine ho to working condition mein; inlet hose / drain sahi ho",
                            "Silk, heavy designer, dry-clean only items pehle alag mark kar dein",
                            "Ghar mein safe, private area jahan paani use ho sake"
                    ),
                    List.of(
                            "Dry clean only garments, heavy stains guarantee",
                            "Loss / damage — reasonable care; valuable items ki zimmedari discuss karke",
                            "Extra load = extra time = possible extra charge"
                    ),
                    VISIT_FLOW
            )),
            Map.entry("WINDOW", new ServiceDetail(
                    "WINDOW",
                    "Window cleaning",
                    "https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=1200&q=85",
                    "Glass, frame aur sill ki safai — andar safe heights; bahar sirf jahan "
                            + "bina risk ke pahunch ho (ground / low floors / safe balcony).",
                    "From ₹149",
                    "Per window (inside + safely reachable outside). High-rise rope/facade jobs priced separately.",
                    List.of(
                            "Glass streak-free cleaning — safe liquid / tools",
                            "Frame, sill, track basic wipe (reachable)",
                            "Before/after quick walkthrough aapke saath",
                            "Grill wali windows — grill ke andar jahan haath jaye wahan"
                    ),
                    List.of(
                            "Reachable windows — high-rise outside glass ke liye rope team alag hoti hai",
                            "Curtains / valuables side mein rakh dein",
                            "Paani aur drainage wali jagah access",
                            "Khaas time slot ho to booking note mein likhein"
                    ),
                    List.of(
                            "High-rise facade / cradle / rope access jobs",
                            "Paint / cement stain removal guarantee (purana stain)",
                            "Broken glass replacement"
                    ),
                    VISIT_FLOW
            )),
            Map.entry("UTENSILS", new ServiceDetail(
                    "UTENSILS",
                    "Utensils & kitchen cleanup",
                    "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=1200&q=85",
                    "Bartan dhona, sink / slab wipe, halka organize — roz ka load ya party ke baad cleanup. "
                            + "Samay pehle tay; zyada kaam = zyada ghante.",
                    "From ₹199",
                    "Per hour kitchen help. Deep chimney, oven chemical clean or extra hours quoted on visit.",
                    List.of(
                            "Utensils wash, rinse, dry stack",
                            "Sink, slab, splash area wipe",
                            "Basic counter organise (safe items)",
                            "Garbage dry/wet aapke dustbin tak — heavy disposal alag ho sakta hai"
                    ),
                    List.of(
                            "Garam paani, liquid soap / scrub basic supplies",
                            "Safe non-slip standing; partner ko kitchen access",
                            "Sharp knives alag rakh dein / bata dein",
                            "Kitne bartan / kitna time — booking description mein clear likhein"
                    ),
                    List.of(
                            "Chimney deep degrease, oven inside heavy chemical clean",
                            "Pest control, drain choke full opening",
                            "Silver / antique polish specialist work"
                    ),
                    VISIT_FLOW
            )),
            Map.entry("BALCONY", new ServiceDetail(
                    "BALCONY",
                    "Balcony cleaning",
                    "https://images.unsplash.com/photo-1493809842364-78817add7ffb?w=1200&q=85",
                    "Balcony / utility area: jhaadu, pocha, cobweb, dry leaves — "
                            + "fresh feel ke liye quick turnaround.",
                    "From ₹279",
                    "Standard balcony or utility cleanup. Very large area, heavy stain or pressure wash may be extra.",
                    List.of(
                            "Dry sweep + wet mop floor",
                            "Railing wipe (reachable)",
                            "Cobweb corners (safe reach)",
                            "Drain nook basic check — heavy choke alag job"
                    ),
                    List.of(
                            "Plants / racks shift karne mein madad agar chahiye to pehle batayein",
                            "Paani tap access",
                            "Kam space / samaan zyada ho to time badh sakta hai — note mein likhein",
                            "High outer facade — yeh service uski height tak limited hai"
                    ),
                    List.of(
                            "Exterior wall pressure wash (high)",
                            "Waterproofing / crack repair",
                            "AC outdoor heavy chemical service"
                    ),
                    VISIT_FLOW
            )),
            Map.entry("BATHROOM", new ServiceDetail(
                    "BATHROOM",
                    "Bathroom cleaning",
                    "https://images.unsplash.com/photo-1552321554-5fefe8c9ef14?w=1200&q=85",
                    "Tiles, fixtures, corners — scrub + disinfect focus. "
                            + "Bathroom size & condition booking note mein batayein taaki sahi time bhejein.",
                    "From ₹399",
                    "One standard bathroom deep clean. Multiple bathrooms, heavy scaling or add-ons quoted before work.",
                    List.of(
                            "Wall tiles, floor, corners scrub",
                            "WC, basin, shower area, taps chrome wipe",
                            "Mirror, glass shine",
                            "Floor disinfect mop finish; mild scale common areas"
                    ),
                    List.of(
                            "Garam paani access agar zarurat ho",
                            "Towels / personal items safe side mein",
                            "Drain slow ho to batayein — full plumbing snake alag",
                            "Multiple bathrooms = alag slot ya extended time"
                    ),
                    List.of(
                            "Permanent stain / paint removal guarantee",
                            "Hidden leak behind tiles",
                            "Ceiling height mould without safe ladder — discuss first"
                    ),
                    VISIT_FLOW
            ))
    );

    private ServiceCatalog() {
    }

    public static Optional<ServiceDetail> find(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(BY_CODE.get(code.trim().toUpperCase(Locale.ROOT)));
    }

    /** Stable order for APIs / listings. */
    public static List<ServiceDetail> allServices() {
        List<ServiceDetail> list = new ArrayList<>(BY_CODE.values());
        list.sort(Comparator.comparing(ServiceDetail::getCode));
        return List.copyOf(list);
    }

    /** Human-readable service name for booking lists (falls back to code). */
    public static String displayTitleForCode(String code) {
        if (code == null || code.isBlank()) {
            return "";
        }
        return find(code).map(ServiceDetail::getTitle).orElse(code.trim().toUpperCase(Locale.ROOT));
    }
}

