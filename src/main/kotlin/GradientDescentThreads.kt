import java.lang.System.currentTimeMillis
import kotlin.concurrent.thread
import kotlin.math.roundToInt
import kotlin.math.sqrt

class GradientDescentThreads(var x: Double, var y: Double, private val step: Double) {
    var z = 0.0
    var gradX = 0.0
    var gradY = 0.0
    var norm = 0.0

    fun gradientDescent(x: Double, y: Double) {
        this.x = x
        this.y = y
        this.z = genFunction()
        this.gradX = gradX()
        this.gradY = gradY()
        this.norm = norm()
    }
    fun genFunction(): Double {
        return 3 * (x - 1) * (x - 1) + y * y
    }

    private fun gradX(): Double {
        return 6 * x - 6
    }

    private fun gradY(): Double {
        return 2 * y
    }

    fun norm(): Double {
        return sqrt((6 * x - 6) * (6 * x - 6) + (2 * y) * (2 * y))
    }

    fun firstPartChangingX() {
        x -= step * gradX / norm
    }

    fun firstPartChangingY() {
        y -= step * gradY / norm
        z = genFunction()
    }

    fun secondPartChangingX() {
        gradX = gradX()
    }
    fun secondPartChangingY() {
        gradY = gradY()
        norm = norm()
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is GradientDescent) false
        else {
            !(this.x != other.x || this.y != other.y)
        }
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + gradX.hashCode()
        result = 31 * result + gradY.hashCode()
        result = 31 * result + norm.hashCode()
        return result
    }

    override fun toString(): String {
        return "x = $x\n" +
                "gradX = $gradX\n" +
                "y = $y\n" +
                "gradY = $gradY\n" +
                "norm = $norm\n" +
                "z = $z\n"
    }

}

fun main() {
    var flag = false
    val m = currentTimeMillis()
    val grad = GradientDescentThreads(x = -0.01, y = -0.67, step = 0.00001)
    // z = 3.49
    grad.gradientDescent(grad.x, grad.y)

    var x = thread {
        for (i in 0..Int.MAX_VALUE) {
            grad.firstPartChangingX()
            grad.secondPartChangingX()
        }

    }
    var y = thread {
        for (i in 0..Int.MAX_VALUE) {
            grad.firstPartChangingY()
            println(grad.toString())
            if ((grad.z * 100000.0).roundToInt() / 100000.0 == 0.00000) {
                flag = true
                break
            }
            grad.secondPartChangingY()
        }
    }
    if (flag) x.interrupt()
    y.join()
    println(currentTimeMillis()-m)
}