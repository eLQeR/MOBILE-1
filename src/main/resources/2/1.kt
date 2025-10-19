import java.util.logging.Logger
import java.util.logging.ConsoleHandler
import java.util.logging.SimpleFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.logging.LogRecord

fun calculateEmissionFactor(Qri: Double, Ar: Double, aVin: Double, etaZu: Double, GVin: Double): Double {
    return (1000000.0 / Qri) * (Ar / 100.0) * aVin * (1 - etaZu) / (1 - GVin / 100.0)
}

fun calculateGrossEmission(k: Double, B: Double, Qri: Double): Double {
    return k * B * Qri / 1000000.0
}

fun main() {
    val logger = Logger.getLogger("EmissionLogger")
    logger.useParentHandlers = false
    val handler = ConsoleHandler()
    handler.formatter = object : SimpleFormatter() {
        override fun format(record: LogRecord): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return "[${dateFormat.format(Date(record.millis))} ${record.level}] ${record.message}\n"
        }
    }
    logger.addHandler(handler)
    
    // Constants
    val etaZu = 0.985 // Efficiency of dust collection
    val Qc = 32.68 // Heat of carbon combustion to CO2, MJ/kg

    // Coal parameters (Variant 3 from Table 2.4)
    val BCoal = 759834.56 // tons
    val QriCoal = 20.47 // MJ/kg
    val ArCoal = 25.20 / 100.0 // Ash content, fraction
    val aVinCoal = 0.80 // Volatile ash fraction for coal with liquid slag removal
    val GVinCoal = 1.5 / 100.0 // Combustible in volatile ash, fraction

    // Fuel oil parameters (Variant 3 from Table 2.4)
    val BMazut = 99672.62 // tons
    val QriMazut = 39.48 // MJ/kg (derived from document)
    val ArMazut = 0.15 / 100.0 // Ash content, fraction
    val aVinMazut = 1.0 // Volatile ash fraction for mazut
    val GVinMazut = 0.0 / 100.0 // Combustible in volatile ash, fraction

    // Natural gas parameters (Variant 3 from Table 2.4)
    val BGas = 115923.14 * 1000 // m³ to liters
    val QriGas = 33.08 // MJ/m³
    val densityGas = 0.723 // kg/m³
    val ArGas = 0.0 // Ash content, fraction (assumed 0 for gas)

    // Calculate emission factors
    val kCoal = calculateEmissionFactor(QriCoal, ArCoal, aVinCoal, etaZu, GVinCoal)
    val kMazut = calculateEmissionFactor(QriMazut, ArMazut, aVinMazut, etaZu, GVinMazut)
    val kGas = 0.0 // No solid particles from natural gas

    // Calculate gross emissions
    val EGrossCoal = calculateGrossEmission(kCoal, BCoal, QriCoal)
    val EGrossMazut = calculateGrossEmission(kMazut, BMazut, QriMazut)
    val EGrossGas = 0.0 // No emission from gas

    // Total gross emission
    val totalEGross = EGrossCoal + EGrossMazut + EGrossGas

    // Output results
    logger.info("Emission results for Variant 3:")
    logger.info("Coal emission factor: %.2f g/GJ".format(kCoal))
    logger.info("Coal gross emission: %.2f tons".format(EGrossCoal))
    logger.info("Fuel oil emission factor: %.2f g/GJ".format(kMazut))
    logger.info("Fuel oil gross emission: %.2f tons".format(EGrossMazut))
    logger.info("Natural gas emission factor: %.2f g/GJ".format(kGas))
    logger.info("Natural gas gross emission: %.2f tons".format(EGrossGas))
    logger.info("Total gross emission: %.2f tons".format(totalEGross))
}