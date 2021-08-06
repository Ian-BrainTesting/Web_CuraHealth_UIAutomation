import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import groovy.json.JsonSlurper as JsonSlurper
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import java.util.concurrent.ThreadLocalRandom as ThreadLocalRandom

JsonSlurper slurper = new JsonSlurper()

//Pre-Condiciones Ejecución
WebUI.callTestCase(findTestCase('LOGIN/TC-008_InicioSesionExitoso'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('HOME_PAGE/TC-004_AccederHistorial'), [:], FailureHandling.STOP_ON_FAILURE)

def wsDoctors = WS.sendRequest(findTestObject('WEB_SERVICES/DummyApi/GET_Doctors'))

WS.verifyResponseStatusCode(wsDoctors, 200, FailureHandling.STOP_ON_FAILURE)

Map responseWsDoctors = slurper.parseText(wsDoctors.getResponseText())

int idDoctor = ThreadLocalRandom.current().nextInt(1, responseWsDoctors.per_page - 1)

String assignedDoctor = (responseWsDoctors.data[idDoctor].first_name + ' ') + responseWsDoctors.data[idDoctor].last_name

def wsCreateAppointment = WS.sendRequest(findTestObject('WEB_SERVICES/CuraHealth/POST_CreateAppointment',
		[('facility') : facility, ('hospital_readmission') : applyHospitalReadmission, ('programs') :typeProgram
			, ('visit_date') : visitDate, ('comment') : comment + " -> Medico Asignado: " + assignedDoctor]))

WS.verifyResponseStatusCode(wsCreateAppointment, 200, FailureHandling.STOP_ON_FAILURE)
//Fin Pre-Condiciones Ejecución

WebUI.refresh()

WebUI.verifyMatch(WebUI.getText(findTestObject('HISTORY/DYN-DIV_DatosCita', [('dateAppointment') : visitDate, ('idData') : 'facility'])),
facility, false, FailureHandling.CONTINUE_ON_FAILURE)

WebUI.verifyMatch(WebUI.getText(findTestObject('HISTORY/DYN-DIV_DatosCita', [('dateAppointment') : visitDate, ('idData') : 'hospital_readmission'])),
applyHospitalReadmission, false, FailureHandling.CONTINUE_ON_FAILURE)

WebUI.verifyMatch(WebUI.getText(findTestObject('HISTORY/DYN-DIV_DatosCita', [('dateAppointment') : visitDate, ('idData') : 'program'])),
typeProgram, false, FailureHandling.CONTINUE_ON_FAILURE)

WebUI.verifyMatch(WebUI.getText(findTestObject('HISTORY/DYN-DIV_DatosCita', [('dateAppointment') : visitDate, ('idData') : 'comment'])),
comment + " -> Medico Asignado: " + assignedDoctor, false, FailureHandling.CONTINUE_ON_FAILURE)