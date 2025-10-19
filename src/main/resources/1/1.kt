import java.util.logging.Logger
import java.util.logging.ConsoleHandler
import java.util.logging.SimpleFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.logging.LogRecord

fun calculateDryAndCombustibleMassCoefficients(wr: Double, ar: Double): Pair<Double, Double> {
    val krs = 100.0 / (100.0 - wr)
    val krg = 100.0 / (100.0 - wr - ar)
    return Pair(krs, krg)
}

fun calculateDryMassComposition(hr: Double, cr: Double, sr: Double, nr: Double, or: Double, ar: Double, krs: Double): List<Double> {
    return listOf(hr * krs, cr * krs, sr * krs, nr * krs, or * krs, ar * krs)
}

fun calculateLowerHeatingValue(cr: Double, hr: Double, or: Double, sr: Double, wr: Double): Double {
    return 339 * cr + 1030 * hr - 108.8 * (or - sr) - 25 * wr
}

fun calculateCombustibleMassComposition(hr: Double, cr: Double, sr: Double, nr: Double, or: Double, krg: Double): List<Double> {
    return listOf(hr * krg, cr * krg, sr * krg, nr * krg, or * krg)
}



fun adjustHeatingValueForMass(qr: Double, wr: Double, krs: Double, krg: Double): Pair<Double, Double> {
    val qd = (qr + 25 * wr) * krs
    val qg = (qr + 25 * wr) * krg
    return Pair(qd / 1000.0, qg / 1000.0) // Convert to MJ/kg
}

fun main() {
    val logger = Logger.getLogger("MassLogger")
    logger.useParentHandlers = false
    val handler = ConsoleHandler()
    handler.formatter = object : SimpleFormatter() {
        override fun format(record: LogRecord): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return "[${dateFormat.format(Date(record.millis))} ${record.level}] ${record.message}\n"
        }
    }
    logger.addHandler(handler)
    
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
    logger.info("Dry mass sum: ${String.format("%.2f", sumDry)}% (should be 100%)")
    logger.info("Combustible mass sum: ${String.format("%.2f", sumComb)}% (should be 100%)")
    logger.info("Task 1 Results for Variant 3:")
    logger.info("Coefficient to dry mass: ${String.format("%.4f", krs)}")
    logger.info("Coefficient to combustible mass: ${String.format("%.4f", krg)}")
    logger.info("Dry mass composition: H= ${String.format("%.3f", dryComposition[0])}%, C= ${String.format("%.3f", dryComposition[1])}%, S= ${String.format("%.3f", dryComposition[2])}%, N= ${String.format("%.3f", dryComposition[3])}%, O= ${String.format("%.3f", dryComposition[4])}%, A= ${String.format("%.3f", dryComposition[5])}%")
    logger.info("Combustible mass composition: H= ${String.format("%.3f", combustibleComposition[0])}%, C= ${String.format("%.3f", combustibleComposition[1])}%, S= ${String.format("%.3f", combustibleComposition[2])}%, N= ${String.format("%.3f", combustibleComposition[3])}%, O= ${String.format("%.3f", combustibleComposition[4])}%")
    logger.info("Lower heating value (working): ${String.format("%.4f", qr / 1000.0)} MJ/kg")
    logger.info("Lower heating value (dry): ${String.format("%.4f", qdMj)} MJ/kg")
    logger.info("Lower heating value (combustible): ${String.format("%.4f", qgMj)} MJ/kg")
}