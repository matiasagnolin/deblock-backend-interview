import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = ["org.deblock.exercise"])
class Deblock

fun main(args: Array<String>) {
    runApplication<Deblock>(*args)
}