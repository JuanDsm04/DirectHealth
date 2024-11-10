package com.uvg.directhealth.data.source

import com.uvg.directhealth.R
import com.uvg.directhealth.data.model.Specialty

fun specialtyToStringResource(specialty: Specialty): Int {
    return when (specialty) {
        Specialty.GENERAL -> R.string.specialty_general
        Specialty.CARDIOLOGY -> R.string.specialty_cardiology
        Specialty.DERMATOLOGY -> R.string.specialty_dermatology
        Specialty.ENDOCRINOLOGY -> R.string.specialty_endocrinology
        Specialty.GASTROENTEROLOGY -> R.string.specialty_gastroenterology
        Specialty.GYNECOLOGY -> R.string.specialty_gynecology
        Specialty.HEMATOLOGY -> R.string.specialty_hematology
        Specialty.INFECTIOLOGY -> R.string.specialty_infectiology
        Specialty.NEPHROLOGY -> R.string.specialty_nephrology
        Specialty.NEUROLOGY -> R.string.specialty_neurology
        Specialty.PULMONOLOGY -> R.string.specialty_pulmonology
        Specialty.OPHTHALMOLOGY -> R.string.specialty_ophthalmology
        Specialty.ONCOLOGY -> R.string.specialty_oncology
        Specialty.ORTHOPEDICS -> R.string.specialty_orthopedics
        Specialty.OTOLARYNGOLOGY -> R.string.specialty_otolaryngology
        Specialty.PEDIATRICS -> R.string.specialty_pediatrics
        Specialty.PSYCHIATRY -> R.string.specialty_psychiatry
        Specialty.RADIOLOGY -> R.string.specialty_radiology
        Specialty.RHEUMATOLOGY -> R.string.specialty_rheumatology
        Specialty.TRAUMATOLOGY -> R.string.specialty_traumatology
        Specialty.UROLOGY -> R.string.specialty_urology
        Specialty.ALLERGOLOGY -> R.string.specialty_allergology
        Specialty.ANGIOLOGY -> R.string.specialty_angiology
        Specialty.PLASTIC -> R.string.specialty_plastic
        Specialty.GERIATRICS -> R.string.specialty_geriatrics
    }
}