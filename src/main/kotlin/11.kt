fun calculateDryAndCombustibleMassCoefficients(wr: Double, ar: Double): Pair<Double, Double> {
    val krs = 100.0 / (100.0 - wr)
    val krg = 100.0 / (100.0 - wr - ar)
    return Pair(krs, krg)
}

fun calculateDryMassComposition(hr: Double, cr: Double, sr: Double, nr: Double, or: Double, ar: Double, krs: Double): List<Double> {
    return listOf(hr * krs, cr * krs, sr * krs, nr * krs, or * krs, ar * krs)
}

fun calculateCombustibleMassComposition(hr: Double, cr: Double, sr: Double, nr: Double, or: Double, krg: Double): List<Double> {
    return listOf(hr * krg, cr * krg, sr * krg, nr * krg, or * krg)
}

fun calculateLowerHeatingValue(cr: Double, hr: Double, or: Double, sr: Double, wr: Double): Double {
    return 339 * cr + 1030 * hr - 108.8 * (or - sr) - 25 * wr
}

fun adjustHeatingValueForMass(qr: Double, wr: Double, krs: Double, krg: Double): Pair<Double, Double> {
    val qd = (qr + 25 * wr) * krs
    val qg = (qr + 25 * wr) * krg
    return Pair(qd / 1000.0, qg / 1000.0) // Convert to MJ/kg
}

fun main() {
    // Task 1: Variant 3 data
    val hr = 3.8
    val cr = 62.4
    val sr = 3.6
    val nr = 1.1
    val or = 4.3
    val wr = 6.0
    val ar = 18.8

    val (krs, krg) = calculateDryAndCombustibleMassCoefficients(wr, ar)
    val dryComposition = calculateDryMassComposition(hr, cr, sr, nr, or, ar, krs)
    val combustibleComposition = calculateCombustibleMassComposition(hr, cr, sr, nr, or, krg)
    val qr = calculateLowerHeatingValue(cr, hr, or, sr, wr)
    val (qdMj, qgMj) = adjustHeatingValueForMass(qr, wr, krs, krg)

    // Output results with checks
    val sumDry = dryComposition.sum()
    val sumComb = combustibleComposition.sum()
    println("Dry mass sum: ${String.format("%.2f", sumDry)}% (should be 100%)")
    println("Combustible mass sum: ${String.format("%.2f", sumComb)}% (should be 100%)")
    println("Task 1 Results for Variant 3:")
    println("Coefficient to dry mass: ${String.format("%.4f", krs)}")
    println("Coefficient to combustible mass: ${String.format("%.4f", krg)}")
    println("Dry mass composition: H= ${String.format("%.3f", dryComposition[0])}%, C= ${String.format("%.3f", dryComposition[1])}%, S= ${String.format("%.3f", dryComposition[2])}%, N= ${String.format("%.3f", dryComposition[3])}%, O= ${String.format("%.3f", dryComposition[4])}%, A= ${String.format("%.3f", dryComposition[5])}%")
    println("Combustible mass composition: H= ${String.format("%.3f", combustibleComposition[0])}%, C= ${String.format("%.3f", combustibleComposition[1])}%, S= ${String.format("%.3f", combustibleComposition[2])}%, N= ${String.format("%.3f", combustibleComposition[3])}%, O= ${String.format("%.3f", combustibleComposition[4])}%")
    println("Lower heating value (working): ${String.format("%.4f", qr / 1000.0)} MJ/kg")
    println("Lower heating value (dry): ${String.format("%.4f", qdMj)} MJ/kg")
    println("Lower heating value (combustible): ${String.format("%.4f", qgMj)} MJ/kg")
}