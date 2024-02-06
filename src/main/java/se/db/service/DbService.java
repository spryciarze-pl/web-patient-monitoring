package se.db.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.db.model.*;
import se.db.repository.*;
import se.dto.DoctorsActivityDto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class DbService {

    @Autowired
    SpecializationRepository specializationRepository;
    @Autowired
    AssignmentRepository assignmentRepository;
    @Autowired
    PatientsActivityRepository patientsActivityRepository;
    @Autowired
    DoctorsActivityRepository doctorsActivityRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ClinicRepository clinicRepository;
    @Autowired
    PasswordRepository passwordRepository;
    @Autowired
    PrescriptionRepository prescriptionRepository;
    @Autowired
    AppointmentRepository appointmentRepository;

    public boolean patientHasDoctor(int patientId) {
        return assignmentRepository.findByPatientId(patientId) != null;
    }

    public User getUserById(int userId) {
        return userRepository.findById(userId);
    }

    public List<User> getPatientsAssignedToDoctor(int doctorId) {
        return assignmentRepository.findByDoctorId(doctorId).stream()
                .map(id -> userRepository.findById(id.getPatientId())).toList();
    }

    public List<DoctorsActivityDto> getActivityDtoByDoctorId(int doctorId) {
        return doctorsActivityRepository.findByDoctorId(doctorId).stream()
                .map(activity -> new DoctorsActivityDto(userRepository.findById(activity.getPatientId()), activity))
                //.sorted(Comparator.comparing(DoctorsActivityDto::getLocalDateTime))
                .toList();
    }

    public List<DoctorsActivityDto> getActivityDtoByPatientId(int patientId) {
        return doctorsActivityRepository.findByPatientId(patientId).stream()
                .filter(activity -> !activity.isCompleted())
                .map(activity -> new DoctorsActivityDto(userRepository.findById(patientId), activity))
                .sorted(Comparator.comparing(DoctorsActivityDto::getLocalDateTime))
                .toList();
    }

    public void setDoctorsActivityAsComplete(int activityId) {
        doctorsActivityRepository.markDoctorsActivityAsComplete(activityId);
    }

    public DoctorsActivity getActivityById(int activityId) {
        return doctorsActivityRepository.findById(activityId);
    }

    public User getPatientsDoctor(int patientId) {
        return userRepository.findById(assignmentRepository.findByPatientId(patientId).getDoctorId());
    }

    public void removeAssignedPatient(int patientId) {
        assignmentRepository.deleteByPatientId(patientId);
    }

    public DoctorsActivity saveNewDoctorsActivity(DoctorsActivity activity) {
        DoctorsActivity savedActivity = doctorsActivityRepository.save(activity);
        return savedActivity;
    }

    public void assignPatientToDoctor(Assignment assignment) {
        assignmentRepository.save(assignment);
    }

    public List<User> getAvailableDoctors() {
        List<Integer> availableDoctorsIds = assignmentRepository.findDoctorsWithMoreThan5Assignments();
        List<User> avaibleDoctors = userRepository.findByRoleId(2);

        avaibleDoctors = avaibleDoctors.stream().filter(user -> !availableDoctorsIds.contains(user.getId())).collect(Collectors.toList());
        return avaibleDoctors;
    }

    public void removeDoctorsActivity(int activityId) {
        doctorsActivityRepository.deleteById((long) activityId);
    }

    public void removeAllPatientsDoctorsActivitiesByPatientId(int patientsId) {
        doctorsActivityRepository.deleteByPatientId(patientsId);
    }

    public List<Clinic> getAllClinics() {
        return clinicRepository.findAll();
    }

    public List<Specialization> getAllSpecializations() {
        return specializationRepository.findAll();
    }

    public Integer savePasswordAndGetNewId(Password password) {
        passwordRepository.save(password);
        return passwordRepository.findByHashedPassword(password.getHashedPassword()).getId();
    }

    public void saveNewUser(User user) {
        userRepository.save(user);
    }

    public void saveNewPrescription(Prescription prescription) {
        prescriptionRepository.save(prescription);
    }

    public void removePrescriptionById(int id) {
        prescriptionRepository.deleteById(id);
    }

    public List<Prescription> getPrescriptionByDoctorId(int doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }

    public List<Prescription> getPrescriptionByPatientId(int patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    public void saveNewPatientActivity(PatientsActivity activity) {
        patientsActivityRepository.save(activity);
    }

    public PatientsActivity getPatientActivityByDoctorsActivityId(int activityId) {
        return patientsActivityRepository.findByDoctorsRequestId(activityId);
    }

    public List<PatientsActivity> getPatientActivityByPatientId(int patientId) {
        return patientsActivityRepository.findByPatientId(patientId);
    }

    public void removeAllPatientsActivitiesForPatientId(int patientId) {
        patientsActivityRepository.deleteByPatientId(patientId);
    }

    public void removePatientActivityByDoctorsRequestId(int doctorsRequestId) {
        patientsActivityRepository.deleteByDoctorsRequestId(doctorsRequestId);
    }

    public List<Appointment> getAppointmentsByDoctorIdAndDrive(int doctorId, boolean doctorDriven) {
        return appointmentRepository.findByDoctorIdAndDoctorDriven(doctorId, doctorDriven);
    }

    public List<Appointment> getAppointmentsByPatientIdAndDrive(int patientId, boolean doctorDriven) {
        return appointmentRepository.findByPatientIdAndDoctorDriven(patientId, doctorDriven);
    }

    public void saveNewAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    public void removeAppointmentById(int appointmentId) {
        appointmentRepository.deleteById((long) appointmentId);
    }

    public void setAppointmentConfirmedById(int appointmentId) {
        appointmentRepository.markAppointmentAsConfirmed(appointmentId);
    }

    public void setAppointmentInvalidByIdAndAddReason(int appointmentId, String reason) {
        appointmentRepository.markAppointmentAsInvalid(appointmentId, reason);
    }

}
