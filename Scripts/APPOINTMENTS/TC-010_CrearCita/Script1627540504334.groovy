import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import java.util.concurrent.ThreadLocalRandom

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
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

WebUI.callTestCase(findTestCase('LOGIN/TC-008_InicioSesionExitoso'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.selectOptionByLabel(findTestObject("APPOINTMENTS/CMB_Facility"), facility, false)

if(applyHospitalReadmission == "Yes") {
	WebUI.click(findTestObject("APPOINTMENTS/CHK_HospitalReadmission"))
}

switch(typeProgram) {
	case "Medicare":
		WebUI.click(findTestObject("APPOINTMENTS/LBL_Medicare"))
		break;

	case "Medicaid":
		WebUI.click(findTestObject("APPOINTMENTS/LBL_Medicaid"))
		break;

	case "None":
		WebUI.click(findTestObject("APPOINTMENTS/LBL_None"))
		break;
}

if(visitDate.isEmpty()) {
	visitDate = CustomKeywords.'com.kms.katalon.keyword.datetime.DateTimeUtility.getCurrentDateTime'(null, "dd/MM/yyyy")
}

WebUI.setText(findTestObject("APPOINTMENTS/INPUT_VisitDate"), visitDate);

WebUI.setText(findTestObject("APPOINTMENTS/TXT_Comment"), comment)

WebUI.click(findTestObject('APPOINTMENTS/BTN_BookAppointment'))

WebUI.verifyElementText(findTestObject("GENERAL_OBJECTS/H2_TitleSection"), "Appointment Confirmation")
