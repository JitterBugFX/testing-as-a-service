package org.kbelow.kotlin.taas

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileWriter

data class WorkerParameter(val name: String)

data class WorkerResult(val name: String, val successful: Boolean)

class Worker(val parameterFileName : String) : Runnable {

	private val log = LogManager.getLogger()
	private val json = jacksonObjectMapper()

	override fun run() {
		log.info("process $parameterFileName")
		val parameter : WorkerParameter = readParameters()
		log.info("rcv $parameter")
		// TODO do some work
		val result = WorkerResult(name = parameter.name, successful = true)
		log.info("snd $result") 
		writeResult(result)
	}
	
	private fun readParameters() : WorkerParameter {
		return json.readValue(File(parameterFileName))
	}
	
	private fun writeResult(result : WorkerResult) : Unit {
		val resultFile = File(parameterFileName.replace("WorkerParameter", "WorkerResult"))
		val resultFileWriter = FileWriter(resultFile)
		json.writeValue(resultFileWriter, result)
		resultFileWriter.close()
	}
}

fun main(args: Array<String>) {	
	Worker(args[0]).run()
}
