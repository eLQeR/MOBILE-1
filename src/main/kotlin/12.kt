fun calculateWorkingMassComposition(cg: Double, hg: Double, og: Double, sg: Double, wr: Double, ad: Double): List<Double> {
    val ar = ad * (100.0 - wr) / 100.0
    val multiplier = (100.0 - wr - ad) / 100.0
    return listOf(cg * multiplier, hg * multiplier, og * multiplier, sg * multiplier, ar)
}

fun adjustVanadiumForWorkingMass(vg: Double, wr: Double): Double {
    return vg * (100.0 - wr) / 100.0
}

fun calculateWorkingHeatingValue(qgMj: Double, wr: Double, ad: Double): Double {
    val multiplier = (100.0 - wr - ad) / 100.0
    return qgMj * multiplier - 0.025 * wr
}

fun main() {
    // Task 2: Control example data adapted for Variant 3 context
    val cg = 85.50
    val hg = 11.20
    val og = 0.80
    val sg = 2.50
    val qgMj = 40.40
    val wr = 2.0
    val ad = 0.15
    val vg = 333.3

    val     workingComposition = calculateWorkingMassComposition(cg, hg, og, sg, wr, ad)
    val vr = adjustVanadiumForWorkingMass(vg, wr)
    val qrMj = calculateWorkingHeatingValue(qgMj, wr, ad)

    println("Task 2 Results using Control Example adapted for Variant 3:")
    println(
        "Working mass composition: H= ${String.format("%.2f", workingComposition[1])}%, C= ${
            String.format(
                "%.2f",
                workingComposition[0]
            )
        }%, S= ${String.format("%.2f", workingComposition[3])}%, O= ${
            String.format(
                "%.2f",
                workingComposition[2]
            )
        }%, A= ${String.format("%.2f", workingComposition[4])}%, V= ${String.format("%.2f", vr)} mg/kg"
    )
    println("Lower heating value (working): ${String.format("%.2f", qrMj)} MJ/kg")
}