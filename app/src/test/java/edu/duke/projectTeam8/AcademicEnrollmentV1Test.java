package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


public class AcademicEnrollmentV1Test {
    @Test
    public void test_createStudent() throws IOException {
        AcademicEnrollmentV1 enrollment = new AcademicEnrollmentV1();

        enrollment.createStudent(new Name("John", "Smith"), "js101", "js101@duke.com");
        OldStudent oldStudent = enrollment.getStudentByNetID("js101");
        assertEquals("John Smith", oldStudent.toString());
        assertThrows(IllegalArgumentException.class, () -> enrollment.getStudentByNetID("js102"));
        assertEquals("js101", oldStudent.getNetID());
        assertEquals("John Smith", enrollment.getStudentByNetID("js101").toString());
    }

    @Test
    public void test_studentExists() throws IOException {
        AcademicEnrollmentV1 enrollment = new AcademicEnrollmentV1();
        enrollment.createStudent(new Name("John", "Smith"), "js101", "js101@duke.edu");
        assertTrue(enrollment.studentExists("js101"));
        assertFalse(enrollment.studentExists("js102"));
    }

    @Test
    public void test_getNumberOfStudents() throws IOException {
        AcademicEnrollmentV1 enrollment = new AcademicEnrollmentV1();
        enrollment.createStudent(new Name("John", "Smith"), "js101", "js101@duke.edu");
        enrollment.createStudent(new Name("Jane", "Smith"), "js102", "js102@duke.edu");
        assertEquals(2, enrollment.getNumberOfStudents());
    }

    @Test
    public void test_XMLSerialize() throws Exception {
        AcademicEnrollmentV1 enrollment = new AcademicEnrollmentV1();
        enrollment.createStudent(new Name("John", "Smith"), "js101", "john@aol.com");
        enrollment.createStudent(new Name("Mike", "Smith"), "ms101", "mike@aol.com");
        OldCourse oldCourse = new OldCourse(new CourseID("Test", 651));
        oldCourse.enrollStudent(enrollment.getStudentByNetID("js101"));
        oldCourse.enrollStudent(enrollment.getStudentByNetID("ms101"));
        OldStudent john = enrollment.getStudentByNetID("js101");
        OldStudent mike = enrollment.getStudentByNetID("ms101");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date1 = dateFormat.parse("10/21/1999");
        Date date2 = dateFormat.parse("12/22/2022");
        john.addAttendanceRecord(oldCourse, date1, new Attendance());
        john.addAttendanceRecord(oldCourse, date2, new Attendance());
        john.getAttendanceRecord(oldCourse).modifyRecord(date1, (attendance) -> attendance.markTardy());
        john.getAttendanceRecord(oldCourse).modifyRecord(date2, (attendance) -> attendance.markAbsent());
        mike.addAttendanceRecord(oldCourse, date1, new Attendance());
        mike.addAttendanceRecord(oldCourse, date2, new Attendance());
        mike.getAttendanceRecord(oldCourse).modifyRecord(date1, (attendance) -> attendance.markAbsent());
        mike.getAttendanceRecord(oldCourse).modifyRecord(date2, (attendance) -> attendance.markAttended());

        Serializer serializedRecords = enrollment.serialize((string1, object) -> new XMLSerializer(string1, object), (string2, objects) -> new XMLSerializer(string2, objects));
        String expectedRecords = "<enrollment>\n\t<student>\n\t\t<first name>John</first name>\n\t\t<last name>Smith</last name>\n\t\t<email>john@aol.com</email>\n\t\t<netID>js101</netID>\n\t\t<course list>\n\t\t\t<course>\n\t\t\t\t<course id>Test651</course id>\n\t\t\t\t<enrollment status>true</enrollment status>\n\t\t\t\t<professor>\n\t\t\t\t\t<first name>Test</first name>\n\t\t\t\t\t<last name>Professor</last name>\n\t\t\t\t\t<email>Test@aol.com</email>\n\t\t\t\t\t<netID>Test101</netID>\n\t\t\t\t</professor>\n\t\t\t\t<attendance records>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Dec 22 00:00:00 EST 2022</date>\n\t\t\t\t\t\t<attendance>Absent</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Oct 21 00:00:00 EDT 1999</date>\n\t\t\t\t\t\t<attendance>Tardy</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t</attendance records>\n\t\t\t</course>\n\t\t</course list>\n\t</student>\n\t<student>\n\t\t<first name>Mike</first name>\n\t\t<last name>Smith</last name>\n\t\t<email>mike@aol.com</email>\n\t\t<netID>ms101</netID>\n\t\t<course list>\n\t\t\t<course>\n\t\t\t\t<course id>Test651</course id>\n\t\t\t\t<enrollment status>true</enrollment status>\n\t\t\t\t<professor>\n\t\t\t\t\t<first name>Test</first name>\n\t\t\t\t\t<last name>Professor</last name>\n\t\t\t\t\t<email>Test@aol.com</email>\n\t\t\t\t\t<netID>Test101</netID>\n\t\t\t\t</professor>\n\t\t\t\t<attendance records>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Dec 22 00:00:00 EST 2022</date>\n\t\t\t\t\t\t<attendance>Attended</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Oct 21 00:00:00 EDT 1999</date>\n\t\t\t\t\t\t<attendance>Absent</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t</attendance records>\n\t\t\t</course>\n\t\t</course list>\n\t</student>\n</enrollment>";
        assertEquals(expectedRecords, serializedRecords.toString());
    }

    @Test
    public void test_XMLParser() throws Exception {
        AcademicEnrollmentV1 enrollment = new AcademicEnrollmentV1();

        String xml = "<enrollment>\n\t<student>\n\t\t<first name>John</first name>\n\t\t<last name>Smith</last name>\n\t\t<email>john@aol.com</email>\n\t\t<netID>js101</netID>\n\t\t<course list>\n\t\t\t<course>\n\t\t\t\t<course id>Test651</course id>\n\t\t\t\t<professor>\n\t\t\t\t\t<first name>Test</first name>\n\t\t\t\t\t<last name>Professor</last name>\n\t\t\t\t\t<email>Test@aol.com</email>\n\t\t\t\t\t<netID>Test101</netID>\n\t\t\t\t</professor>\n\t\t\t\t<attendance records>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Dec 22 00:00:00 EST 2022</date>\n\t\t\t\t\t\t<attendance>Absent</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Oct 21 00:00:00 EDT 1999</date>\n\t\t\t\t\t\t<attendance>Tardy</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t</attendance records>\n\t\t\t</course>\n\t\t</course list>\n\t</student>\n\t<student>\n\t\t<first name>Mike</first name>\n\t\t<last name>Smith</last name>\n\t\t<email>mike@aol.com</email>\n\t\t<netID>ms101</netID>\n\t\t<course list>\n\t\t\t<course>\n\t\t\t\t<course id>Test651</course id>\n\t\t\t\t<professor>\n\t\t\t\t\t<first name>Test</first name>\n\t\t\t\t\t<last name>Professor</last name>\n\t\t\t\t\t<email>Test@aol.com</email>\n\t\t\t\t\t<netID>Test101</netID>\n\t\t\t\t</professor>\n\t\t\t\t<attendance records>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Dec 22 00:00:00 EST 2022</date>\n\t\t\t\t\t\t<attendance>Attended</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Oct 21 00:00:00 EDT 1999</date>\n\t\t\t\t\t\t<attendance>Absent</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t</attendance records>\n\t\t\t</course>\n\t\t</course list>\n\t</student>\n</enrollment>";
        enrollment.parseAcademicEnrollment(xml);
        assertTrue(enrollment.studentExists("js101"));
        assertTrue(enrollment.studentExists("ms101"));
        OldStudent john = enrollment.getStudentByNetID("js101");
        OldStudent mike = enrollment.getStudentByNetID("ms101");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date1 = dateFormat.parse("10/21/1999");
        Date date2 = dateFormat.parse("12/22/2022");
        Set<String> johnCourseIDs = new HashSet<>();
        for (Course johnCourse : john.getClasses()) {
            OldCourse johnOldCourse = (OldCourse) johnCourse;
            johnCourseIDs.add(johnOldCourse.toString());
            assertEquals("Tardy",
                    john.getAttendanceRecord(johnOldCourse).getAttendanceForDate(date1).toString());
            assertEquals("Absent",
                    john.getAttendanceRecord(johnOldCourse).getAttendanceForDate(date2).toString());
            assertEquals("Test@aol.com", johnOldCourse.getProfessor().getEmail());
            assertTrue(johnOldCourse.getProfessor().getCourses().contains(johnOldCourse));
        }
        assertTrue(johnCourseIDs.contains("Test651"));
        Set<String> mikeCourseIDs = new HashSet<>();
        for (Course mikeCourse : mike.getClasses()) {
            OldCourse mikeOldCourse = (OldCourse) mikeCourse;
            mikeCourseIDs.add(mikeOldCourse.toString());
            assertEquals("Absent",
                    mike.getAttendanceRecord(mikeOldCourse).getAttendanceForDate(date1).toString());
            assertEquals("Attended",
                    mike.getAttendanceRecord(mikeOldCourse).getAttendanceForDate(date2).toString());
            assertEquals("Test Professor", mikeOldCourse.getProfessor().toString());
            assertTrue(mikeOldCourse.getProfessor().getCourses().contains(mikeOldCourse));
        }
        assertTrue(mikeCourseIDs.contains("Test651"));
    }
    @Test
    public void test_miscellaneous() throws IOException {
        String outputFileName =
                "src/test/resources/academicEnrollmentTestOutput.txt";
        AcademicEnrollmentV1 enrollment = new AcademicEnrollmentV1(outputFileName);
        enrollment.loadAcademicEnrollment();

        enrollment.addProfessor(new Name("Lucas","Larcher"),"ll000",null);
        OldProfessor fakeProfessor = enrollment.findProfessorInDirectory("ll000");
        assertNull(enrollment.getDatabase());
        assertFalse(enrollment.isStudent(fakeProfessor));

    }
    @Test
    public void test_XMLWrite() throws  Exception {
        String outputFileName =
                "src/test/resources/academicEnrollmentTestOutput.txt";
        AcademicEnrollmentV1 enrollment = new AcademicEnrollmentV1(outputFileName);
        enrollment.createStudent(new Name("John", "Smith"), "js101", "john@aol.com");
        enrollment.createStudent(new Name("Mike", "Smith"), "ms101", "mike@aol.com");
        OldCourse oldCourse = new OldCourse(new CourseID("Test", 651));
        oldCourse.enrollStudent(enrollment.getStudentByNetID("js101"));
        oldCourse.enrollStudent(enrollment.getStudentByNetID("ms101"));
        OldStudent john = enrollment.getStudentByNetID("js101");
        OldStudent mike = enrollment.getStudentByNetID("ms101");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date1 = dateFormat.parse("10/21/1999");
        Date date2 = dateFormat.parse("12/22/2022");
        john.addAttendanceRecord(oldCourse, date1, new Attendance());
        john.addAttendanceRecord(oldCourse, date2, new Attendance());
        john.getAttendanceRecord(oldCourse).modifyRecord(date1, (attendance) ->
                attendance.markTardy());
        john.getAttendanceRecord(oldCourse).modifyRecord(date2, (attendance) ->
                attendance.markAbsent());
        mike.addAttendanceRecord(oldCourse, date1, new Attendance());
        mike.addAttendanceRecord(oldCourse, date2, new Attendance());
        mike.getAttendanceRecord(oldCourse).modifyRecord(date1, (attendance) ->
                attendance.markAbsent());
        mike.getAttendanceRecord(oldCourse).modifyRecord(date2, (attendance) ->
                attendance.markAttended());
        enrollment.saveAcademicEnrollment();
        String expectedRecords = "<enrollment>\n\t<student>\n\t\t<first name>John</first name>\n\t\t<last name>Smith</last name>\n\t\t<email>john@aol.com</email>\n\t\t<netID>js101</netID>\n\t\t<course list>\n\t\t\t<course>\n\t\t\t\t<course id>Test651</course id>\n\t\t\t\t<enrollment status>true</enrollment status>\n\t\t\t\t<professor>\n\t\t\t\t\t<first name>Test</first name>\n\t\t\t\t\t<last name>Professor</last name>\n\t\t\t\t\t<email>Test@aol.com</email>\n\t\t\t\t\t<netID>Test101</netID>\n\t\t\t\t</professor>\n\t\t\t\t<attendance records>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Dec 22 00:00:00 EST 2022</date>\n\t\t\t\t\t\t<attendance>Absent</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Oct 21 00:00:00 EDT 1999</date>\n\t\t\t\t\t\t<attendance>Tardy</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t</attendance records>\n\t\t\t</course>\n\t\t</course list>\n\t</student>\n\t<student>\n\t\t<first name>Mike</first name>\n\t\t<last name>Smith</last name>\n\t\t<email>mike@aol.com</email>\n\t\t<netID>ms101</netID>\n\t\t<course list>\n\t\t\t<course>\n\t\t\t\t<course id>Test651</course id>\n\t\t\t\t<enrollment status>true</enrollment status>\n\t\t\t\t<professor>\n\t\t\t\t\t<first name>Test</first name>\n\t\t\t\t\t<last name>Professor</last name>\n\t\t\t\t\t<email>Test@aol.com</email>\n\t\t\t\t\t<netID>Test101</netID>\n\t\t\t\t</professor>\n\t\t\t\t<attendance records>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Dec 22 00:00:00 EST 2022</date>\n\t\t\t\t\t\t<attendance>Attended</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Oct 21 00:00:00 EDT 1999</date>\n\t\t\t\t\t\t<attendance>Absent</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t</attendance records>\n\t\t\t</course>\n\t\t</course list>\n\t</student>\n</enrollment>";
        StringCryptoUtilAES crypto = new StringCryptoUtilAES("TempPassword");
        Path filePath = Paths.get(outputFileName);
        byte[] fileBytes = Files.readAllBytes(filePath);
        String encryptedText = new String(fileBytes);
        String plaintext = crypto.doDecryption(encryptedText);
            assertEquals(expectedRecords, plaintext);

    }

    @Test
    public void test_XMLRead() throws Exception{
        String outputFileName = "src/test/resources/academicEnrollmentTestOutput.txt";
        AcademicEnrollmentV1 enrollment = new AcademicEnrollmentV1(outputFileName);
        enrollment.loadAcademicEnrollment();
        String output = enrollment.getSerializedStr();
        System.out.println(output);
        String expectedRecords = "<enrollment>\n\t<student>\n\t\t<first name>John</first name>\n\t\t<last name>Smith</last name>\n\t\t<email>john@aol.com</email>\n\t\t<netID>js101</netID>\n\t\t<course list>\n\t\t\t<course>\n\t\t\t\t<course id>Test651</course id>\n\t\t\t\t<enrollment status>true</enrollment status>\n\t\t\t\t<professor>\n\t\t\t\t\t<first name>Test</first name>\n\t\t\t\t\t<last name>Professor</last name>\n\t\t\t\t\t<email>Test@aol.com</email>\n\t\t\t\t\t<netID>Test101</netID>\n\t\t\t\t</professor>\n\t\t\t\t<attendance records>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Dec 22 00:00:00 EST 2022</date>\n\t\t\t\t\t\t<attendance>Absent</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Oct 21 00:00:00 EDT 1999</date>\n\t\t\t\t\t\t<attendance>Tardy</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t</attendance records>\n\t\t\t</course>\n\t\t</course list>\n\t</student>\n\t<student>\n\t\t<first name>Mike</first name>\n\t\t<last name>Smith</last name>\n\t\t<email>mike@aol.com</email>\n\t\t<netID>ms101</netID>\n\t\t<course list>\n\t\t\t<course>\n\t\t\t\t<course id>Test651</course id>\n\t\t\t\t<enrollment status>true</enrollment status>\n\t\t\t\t<professor>\n\t\t\t\t\t<first name>Test</first name>\n\t\t\t\t\t<last name>Professor</last name>\n\t\t\t\t\t<email>Test@aol.com</email>\n\t\t\t\t\t<netID>Test101</netID>\n\t\t\t\t</professor>\n\t\t\t\t<attendance records>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Dec 22 00:00:00 EST 2022</date>\n\t\t\t\t\t\t<attendance>Attended</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t\t<record>\n\t\t\t\t\t\t<date>Thu Oct 21 00:00:00 EDT 1999</date>\n\t\t\t\t\t\t<attendance>Absent</attendance>\n\t\t\t\t\t</record>\n\t\t\t\t</attendance records>\n\t\t\t</course>\n\t\t</course list>\n\t</student>\n</enrollment>";
        assertEquals(expectedRecords,output);
        assertTrue(enrollment.studentExists("js101"));
        assertTrue(enrollment.studentExists("ms101"));

    }


    @Test
    public void test_loadFakeFile() throws Exception {
        String fakeFileName = "fakeFile.txt.csv";
        AcademicEnrollmentV1 enrollment = new AcademicEnrollmentV1(fakeFileName);
        assertThrows(FileNotFoundException.class, enrollment::loadAcademicEnrollment);
        String serializedRecords = enrollment.getSerializedStr();
        assertEquals("<enrollment>\n</enrollment>", serializedRecords);
    }

    @Test
    public void test_writeAndLoadEncrpyted() throws Exception {

        AcademicEnrollmentV1 enrollment = new AcademicEnrollmentV1("src/test/resources/CryptoTest.txt");
        enrollment.createStudent(new Name("John", "Smith"), "js101", "john@aol.com");
        enrollment.createStudent(new Name("Mike", "Smith"), "ms101", "mike@aol.com");
        OldCourse oldCourse = new OldCourse(new CourseID("Test", 651));
        oldCourse.enrollStudent(enrollment.getStudentByNetID("js101"));
        oldCourse.enrollStudent(enrollment.getStudentByNetID("ms101"));
        OldStudent john = enrollment.getStudentByNetID("js101");
        OldStudent mike = enrollment.getStudentByNetID("ms101");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date1 = dateFormat.parse("10/21/1999");
        Date date2 = dateFormat.parse("12/22/2022");
        john.addAttendanceRecord(oldCourse, date1, new Attendance());
        john.addAttendanceRecord(oldCourse, date2, new Attendance());
        john.getAttendanceRecord(oldCourse).modifyRecord(date1, (attendance) -> attendance.markTardy());
        john.getAttendanceRecord(oldCourse).modifyRecord(date2, (attendance) -> attendance.markAbsent());
        mike.addAttendanceRecord(oldCourse, date1, new Attendance());
        mike.addAttendanceRecord(oldCourse, date2, new Attendance());
        mike.getAttendanceRecord(oldCourse).modifyRecord(date1, (attendance) -> attendance.markAbsent());
        mike.getAttendanceRecord(oldCourse).modifyRecord(date2, (attendance) -> attendance.markAttended());


        enrollment.saveAcademicEnrollment();
        AcademicEnrollmentV1 enrollment2 = new AcademicEnrollmentV1("src/test/resources/CryptoTest.txt");
        enrollment2.loadAcademicEnrollment();

        String e1Content = enrollment.getSerializedStr();

        String e2Content = enrollment2.getSerializedStr();
        assertEquals(e1Content, e2Content);
    }
    @Test
    public void test_writeAndLoadEncrypted2()throws Exception{
        AcademicEnrollmentV1 ae = new AcademicEnrollmentV1();
        ae.plainTextLoadAcademicEnrollment(".persistentData.xml");
        String content =ae.getSerializedStr();
        ae.saveAcademicEnrollment(".persistentDataEncrypted.xml");
        AcademicEnrollmentV1 ae1 = new AcademicEnrollmentV1(".persistentDataEncrypted.xml");
        ae1.loadAcademicEnrollment();
        assertEquals(content,ae1.getSerializedStr());
    }
}
