import os
import psycopg2
import faker
from dotenv import load_dotenv

print("Starting mock data generation...")

# Load environment variables
load_dotenv()
HOST = os.getenv('HOST')
PORT = os.getenv('PORT')
DBNAME = os.getenv('DBNAME')
USER = os.getenv('USER')
PASSWORD = os.getenv('PASSWORD')
print("> Loaded environment variables")
# -----------------------------

# Connect to database
conn = psycopg2.connect(host=HOST, port=PORT, dbname=DBNAME, user=USER, password=PASSWORD)
cur = conn.cursor()
cur.execute("SELECT version();")
db_version = cur.fetchone()
print("> You are connected to - ", db_version, "\n")

# Generate mock data
fake = faker.Faker()

roles = [
    {"name": "admin", "description": "Administrator role with full platform management privileges.", "sample_permission_1": True, "sample_permission_2": True, "sample_permission_3": True},
    {"name": "patient", "description": "Patient role with restricted access for personal health monitoring.", "sample_permission_1": False, "sample_permission_2": False, "sample_permission_3": True},
    {"name": "doctor", "description": "Doctor role with access to patient information and medical records.", "sample_permission_1": False, "sample_permission_2": True, "sample_permission_3": True}
]

# const specialization table -> name, description
specializations = [
    {"name": "Cardiology", "description": "Cardiology is a branch of medicine that deals with the disorders of the heart as well as some parts of the circulatory system."},
    {"name": "Dermatology", "description": "Dermatology is the branch of medicine dealing with the skin, nails, hair and its diseases."},
    {"name": "Endocrinology", "description": "Endocrinology is a branch of biology and medicine dealing with the endocrine system, its diseases, and its specific secretions known as hormones."},
    {"name": "Gastroenterology", "description": "Gastroenterology is the branch of medicine focused on the digestive system and its disorders."},
    {"name": "Hematology", "description": "Hematology, also spelled haematology, is the branch of medicine concerned with the study of the cause, prognosis, treatment, and prevention of diseases related to blood."},
    {"name": "Infectious Disease", "description": "Infectious diseases, also known as infectiology, is a medical specialty dealing with the diagnosis, control and treatment of infections."},
    {"name": "Nephrology", "description": "Nephrology is a specialty of medicine and pediatrics that concerns itself with the kidneys: the study of normal kidney function and kidney disease, the preservation of kidney health, and the treatment of kidney disease, from diet and medication to renal replacement therapy."},
    {"name": "Neurology", "description": "Neurology is a branch of medicine dealing with disorders of the nervous system."},
    {"name": "Oncology", "description": "Oncology is a branch of medicine that deals with the prevention, diagnosis, and treatment of cancer."},
    {"name": "Pulmonology", "description": "Pulmonology is a medical speciality that deals with diseases involving the respiratory tract."},
    {"name": "Rheumatology", "description": "Rheumatology is a branch of medicine devoted to the diagnosis and therapy of rheumatic diseases."},
    {"name": "Pediatrics", "description": "Pediatrics is the branch of medicine that involves the medical care of infants, children, and adolescents."},
    {"name": "Psychiatry", "description": "Psychiatry is the medical specialty devoted to the diagnosis, prevention, and treatment of mental disorders."},
    {"name": "Ophthalmology", "description": "Ophthalmology is a branch of medicine and surgery which deals with the diagnosis and treatment of eye disorders."},
    {"name": "Obstetrics and Gynecology", "description": "Obstetrics and gynecology is the medical specialty that encompasses the two subspecialties of obstetrics and gynecology."},
    {"name": "Allergy and Immunology", "description": "Allergy and immunology is a medical specialty focused on allergy (allergic diseases) and immunology (immune system diseases)."}
]


# Generate mock users -> name, surname, personal_identification_number, phone, mail, registration timestamp (default in postgres), confirmed -> true, password_id, role_id, clinic_id, specialization_id
users = []
for i in range(100):
    fname = fake.first_name()
    lname = fake.last_name() 
    users.append((fname, lname, fake.ssn(), fake.phone_number(), (fname.lower() + "." + lname.lower() + "@gmail.com"), i+1))

print("> Generated mock users")

# Generate mock passwords
passwords = []
for i in range(100):
    passwords.append(fake.password())

print("> Generated mock passwords")

# Generate mock clinics -> name, address, phone, mail
clinics = []
for i in range(10):
    company = fake.company()
    clinics.append((fake.company(), fake.address(), fake.phone_number(), fake.company_email()))

print("> Generated mock clinics")

admins_nums = [1, 2]
doctors = []

# doctors randomly assigned from 3 to 100, to each assign one clinic and one specialization
for i in range(2*len(clinics)):
    doctors.append((fake.random_int(min=3, max=100), fake.random_int(min=1, max=len(clinics)), fake.random_int(min=1, max=len(specializations))))

print("> Generated mock doctors")


medicines = [
    "Paracetamol",
    "Ibuprofen",
    "Aspirin",
    "Amoxicillin",
    "Omeprazole",
    "Codeine",
    "Metformin",
    "Atorvastatin",
    "Prednisone",
    "Azithromycin",
    "Ciprofloxacin",
    "Fluoxetine",
    "Gabapentin",
    "Hydrochlorothiazide",
    "Lisinopril",
    "Lorazepam",
    "Losartan",
    "Metoprolol",
    "Oxycodone",
    "Sertraline",
    "Simvastatin",
    "Tramadol",
    "Trazodone",
    "Warfarin",
    "Zolpidem"
]

# Mock prescriptions -> doctor_id, patient_id, medicine
prescriptions = []
for i in range(42):
    # randomly select doctor
    doctor = doctors[fake.random_int(min=0, max=len(doctors)-1)]
    # get doctor's id
    doctor_id = doctor[0]
    # randomly select patient id -> it can't be the same as doctor's or admin's id
    while True:
        patient_id = fake.random_int(min=3, max=100)
        # check if patient_id is not the same as in doctors list
        for d in doctors:
            if d[0] == patient_id:
                continue
        break
    # randomly select medicine
    medicine = medicines[fake.random_int(min=0, max=len(medicines)-1)]
    prescriptions.append((doctor_id, patient_id, medicine))
    
print("> Generated mock prescriptions")

# -----------------------------
    
# Insert mock data into database
# Insert roles
for role in roles:
    insert_data_query = "INSERT INTO roles (name, description, sample_permission_1, sample_permission_2, sample_permission_3) VALUES (%s, %s, %s, %s, %s)"
    data = (role["name"], role["description"], role["sample_permission_1"], role["sample_permission_2"], role["sample_permission_3"])
    cur.execute(insert_data_query, data)

print("> Inserted mock roles")

# Insert passwords
for password in passwords:
    insert_data_query = "INSERT INTO passwords (hashed_password) VALUES (%s)"
    # Pass the password as a single-element tuple
    cur.execute(insert_data_query, (password,))

print("> Inserted mock passwords")

# Insert specializations
for specialization in specializations:
    insert_data_query = "INSERT INTO specializations (name, description) VALUES (%s, %s)"
    data = (specialization["name"], specialization["description"])
    cur.execute(insert_data_query, data)

print("> Inserted mock specializations")

# Insert clinics
for clinic in clinics:
    insert_data_query = "INSERT INTO clinics (name, address, phone, mail) VALUES (%s, %s, %s, %s)"
    data = tuple(clinic)
    cur.execute(insert_data_query, data)

print("> Inserted mock clinics")

# Insert users
for user in users:
    insert_data_query = "INSERT INTO users (name, surname, pin, phone, mail, password_id, role_id) VALUES (%s, %s, %s, %s, %s, %s, 2)"
    data = tuple(user)
    cur.execute(insert_data_query, data)

# Change user role to admin
for admin_num in admins_nums:
    update_data_query = "UPDATE users SET role_id = 1 WHERE id = %s"
    cur.execute(update_data_query, (admin_num,))

# Change user role to doctor
for doctor in doctors:
    # update role, clinic and specialization
    update_data_query = "UPDATE users SET role_id = 3, clinic_id = %s, specialization_id = %s WHERE id = %s"
    cur.execute(update_data_query, (doctor[1], doctor[2], doctor[0]))

print("> Inserted mock users")

# Insert prescriptions
for prescription in prescriptions:
    insert_data_query = "INSERT INTO prescriptions (doctor_id, patient_id, medicine) VALUES (%s, %s, %s)"
    data = tuple(prescription)
    cur.execute(insert_data_query, data)

print("> Inserted mock prescriptions")

# -----------------------------

# End connection
conn.commit()
print("\n> Data inserted successfully")
cur.close()
conn.close()
print("Connection closed...")
