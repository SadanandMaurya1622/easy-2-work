package com.easy2work.catalog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class ServiceCatalog {

    private static final List<String> VISIT_FLOW = List.of(
            "You book on the website with your address and a short note; you receive a reference number.",
            "A verified partner accepts the job and may call or message on WhatsApp to confirm timing.",
            "At your home, the scope is explained first; work starts only after you agree.",
            "Payment follows your booking terms (app / UPI / cash) — details are clarified on the call."
    );

    private static final Map<String, ServiceDetail> BY_CODE = Map.ofEntries(
            Map.entry("ELECTRICAL", new ServiceDetail(
                    "ELECTRICAL",
                    "Electrical repair",
                    "https://images.unsplash.com/photo-1621905252507-b35492cc74b4?w=1200&q=85",
                    "Safe electrical checks and repairs at home — small jobs or common issues such as wiring, switches, or MCB. "
                            + "Your partner brings standard tools; safety first, then the fix.",
                    "From ₹199",
                    "Visit plus basic diagnosis. Spare parts, concealed wiring, and heavy rework are billed separately after an on-site estimate.",
                    List.of(
                            "Fault finding: switch, socket, loose wiring, tripping MCB (basic level)",
                            "Tube light, bulb holder, fan connection / basic fitting (within scope)",
                            "Temporary safe isolation or guidance if major rework or new wiring is required",
                            "Brief estimate up front — we proceed only after you approve"
                    ),
                    List.of(
                            "Access to main power / MCB board (safe, clutter-free)",
                            "A stool or ladder for high points — or tell us so we send the right partner",
                            "Separate power points or earthing for new heavy appliances — mention in advance",
                            "Unsafe or unauthorised old wiring may need correction before we continue"
                    ),
                    List.of(
                            "Full home rewiring, concealed conduit, heavy industrial panels",
                            "In-warranty repairs inside branded appliances (use OEM service)",
                            "Materials billed separately — parts used are charged per on-site estimate",
                            "Fire or shock emergency — call your utility or emergency services immediately"
                    ),
                    VISIT_FLOW
            )),
            Map.entry("AC", new ServiceDetail(
                    "AC",
                    "AC servicing",
                    "https://images.unsplash.com/photo-1631545914464-f152c32b1b2c?w=1200&q=85",
                    "Weak cooling, odours, or a pre-season service? "
                            + "Filter and coil cleaning, drain checks, and a basic performance test — at home.",
                    "From ₹449",
                    "Per split AC standard service. Gas top-up, PCB repair, and spare parts charged based on actual use.",
                    List.of(
                            "Indoor unit: filter wash, visible coil and blower-area cleaning (within scope)",
                            "Outdoor unit: dust cleanup and fin check where safely reachable",
                            "Drain line flush and basic clog check; common leak points",
                            "Cooling test run; if gas or compressor issues are suspected, we explain next steps"
                    ),
                    List.of(
                            "Safe access to the AC — balcony, window, or service platform",
                            "Power on; remote available",
                            "Clear drain path — tell us if there are known blockages",
                        "Large gas refill or compressor jobs may need a separate visit and rate"
                    ),
                    List.of(
                            "Full gas top-up without a proper leak check",
                            "PCB / inverter-board-level electronic repair (specialist work)",
                            "Scaffolding or rope access for unsafe outdoor units — you may need to arrange this",
                            "Spare parts (gas, capacitor, etc.) — charged for actual use"
                    ),
                    VISIT_FLOW
            )),
            Map.entry("COOLER", new ServiceDetail(
                    "COOLER",
                    "Cooler repair",
                    "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=1200&q=85",
                    "Cooler not cooling, pump or fan fault, or water overflow? "
                            + "Basic repair and seasonal tune-up at home.",
                    "From ₹249",
                    "Basic repair and seasonal tune-up. New motor, pads, or major body work quoted after inspection.",
                    List.of(
                            "Pump, motor, fan belt / coupling — basic check and repair",
                            "Cooling pad condition check; replacement guidance and fitting if parts are arranged",
                            "Water distribution pipe, float valve, overflow issues (reachable parts)",
                            "Season open/close basic service with cleaning tips"
                    ),
                    List.of(
                            "Power socket and water supply near the cooler",
                            "Unit should be accessible — mention if space is tight",
                            "Replacement pads or motor cost extra",
                            "Large body cracks or tank leaks may need a workshop job"
                    ),
                    List.of(
                            "Full motor rewinding in a workshop",
                            "Permanent weld of large plastic cracks",
                            "Brand-specific spares if unavailable — delays possible"
                    ),
                    VISIT_FLOW
            )),
            Map.entry("LAUNDRY", new ServiceDetail(
                    "LAUNDRY",
                    "Laundry help",
                    "https://images.unsplash.com/photo-1582735689369-4fe89db7114c?w=1200&q=85",
                    "Washing, drying, and ironing help at home — for a busy week or after guests. "
                            + "Load size and time are agreed up front.",
                    "From ₹299",
                    "Per agreed session (load and duration). Extra load, dry-clean-only items, or special care may cost more.",
                    List.of(
                            "Machine or bucket wash — as agreed in the booking",
                            "Drying (rack, sun, or limited spin) and folding",
                            "Everyday ironing — count or duration per booking",
                            "Care labels respected — flag delicate items separately"
                    ),
                    List.of(
                            "Detergent, bucket, drying space or pegs — basic setup at your place",
                            "Working washing machine; inlet and drain OK",
                            "Separate silk, heavy designer, or dry-clean-only items in advance",
                            "A safe, private area where water can be used"
                    ),
                    List.of(
                            "Dry-clean-only garments or guaranteed stain removal",
                            "Loss or damage — we take reasonable care; discuss valuables beforehand",
                            "Extra load means extra time and possible extra charge"
                    ),
                    VISIT_FLOW
            )),
            Map.entry("WINDOW", new ServiceDetail(
                    "WINDOW",
                    "Window cleaning",
                    "https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=1200&q=85",
                    "Glass, frame, and sill cleaning — inside at safe heights; outside only where we can reach safely "
                            + "(ground floor, low floors, safe balcony).",
                    "From ₹149",
                    "Per window (inside plus safely reachable outside). High-rise rope or facade jobs priced separately.",
                    List.of(
                            "Glass cleaned streak-free with safe products and tools",
                            "Frame, sill, and track — basic wipe where reachable",
                            "Quick before/after walkthrough with you",
                            "Grilled windows — inside the grill where the hand can safely reach"
                    ),
                    List.of(
                            "Reachable windows — outside glass on high floors needs a rope team",
                        "Move curtains and valuables aside",
                        "Access to water and drainage",
                        "Note preferred time slots in your booking if needed"
                    ),
                    List.of(
                            "High-rise facade, cradle, or rope-access jobs",
                            "Guaranteed removal of old paint or cement stains",
                            "Broken glass replacement"
                    ),
                    VISIT_FLOW
            )),
            Map.entry("UTENSILS", new ServiceDetail(
                    "UTENSILS",
                    "Utensils & kitchen cleanup",
                    "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=1200&q=85",
                    "Washing dishes, wiping sink and counter, light tidy-up — daily load or post-party cleanup. "
                            + "Time is agreed up front; more work means more time.",
                    "From ₹199",
                    "Per hour of kitchen help. Deep chimney, oven chemical clean, or extra hours quoted on the visit.",
                    List.of(
                            "Wash, rinse, dry, and stack utensils",
                            "Wipe sink, counter, and splashback",
                            "Light counter organisation (safe items only)",
                            "Dry and wet rubbish to your bins — heavy disposal may be separate"
                    ),
                    List.of(
                            "Hot water, washing-up liquid, basic scrubbers",
                            "Non-slip floor and clear kitchen access",
                            "Keep sharp knives separate or tell us where they are",
                            "Describe load size and duration clearly in the booking"
                    ),
                    List.of(
                            "Deep chimney degrease or heavy oven chemical clean",
                            "Full pest control or drain unblock",
                            "Specialist polish for silver or antiques"
                    ),
                    VISIT_FLOW
            )),
            Map.entry("BALCONY", new ServiceDetail(
                    "BALCONY",
                    "Balcony cleaning",
                    "https://images.unsplash.com/photo-1493809842364-78817add7ffb?w=1200&q=85",
                    "Balcony or utility area: sweep, mop, cobwebs, dry leaves — "
                            + "a quick refresh.",
                    "From ₹279",
                    "Standard balcony or utility cleanup. Very large areas, heavy stains, or pressure washing may cost extra.",
                    List.of(
                            "Dry sweep and wet mop of floor",
                            "Wipe railing where reachable",
                            "Cobwebs in corners within safe reach",
                            "Basic drain nook check — heavy blockages are a separate job"
                    ),
                    List.of(
                            "Tell us if you need help moving plants or racks",
                        "Tap access for water",
                        "Tight space or lots of items may need more time — mention in the note",
                        "High outer walls are outside this service’s height limit"
                    ),
                    List.of(
                            "Exterior wall pressure washing at height",
                            "Waterproofing or crack repair",
                            "Heavy chemical clean of outdoor AC unit"
                    ),
                    VISIT_FLOW
            )),
            Map.entry("BATHROOM", new ServiceDetail(
                    "BATHROOM",
                    "Bathroom cleaning",
                    "https://images.unsplash.com/photo-1552321554-5fefe8c9ef14?w=1200&q=85",
                    "Tiles, fixtures, and corners — scrubbing with disinfectant focus. "
                            + "Note bathroom size and condition so we allocate enough time.",
                    "From ₹399",
                    "One standard bathroom deep clean. Multiple bathrooms, heavy limescale, or add-ons quoted before work.",
                    List.of(
                            "Scrub wall tiles, floor, and corners",
                            "WC, basin, shower area, and chrome taps",
                            "Mirror and glass shine",
                            "Disinfect mop finish; mild scale on common areas"
                    ),
                    List.of(
                            "Hot water access if needed",
                            "Towels and personal items kept aside",
                            "Mention slow drains — full plumbing snake is separate",
                            "Multiple bathrooms need a longer slot or separate booking"
                    ),
                    List.of(
                            "Guaranteed removal of permanent stains or paint",
                            "Hidden leaks behind tiles",
                            "High-ceiling mould without a safe ladder — discuss first"
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
