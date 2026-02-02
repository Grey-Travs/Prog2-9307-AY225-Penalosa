// Prelim Grade Calculator - JavaScript Version
// Lab Work 3

// Constants
const ATTENDANCE_WEIGHT = 0.40;
const LAB_WORK_WEIGHT = 0.60;
const CLASS_STANDING_WEIGHT = 0.30;
const PRELIM_EXAM_WEIGHT = 0.70;
const PASSING_GRADE = 75.0;
const EXCELLENT_GRADE = 100.0;

/**
 * Validates if input is a valid number within range
 */
function isValidInput(value) {
    return !isNaN(value) && parseFloat(value) >= 0 && parseFloat(value) <= 100;
}

/**
 * Gets user input with validation (for console-based version)
 */
function getInputConsole(prompt) {
    let value = prompt(prompt);
    
    while (value === null || !isValidInput(value)) {
        if (value === null) {
            alert("Input cancelled. Please try again.");
            value = prompt(prompt);
        } else {
            alert("⚠️ Invalid input. Please enter a number between 0 and 100.");
            value = prompt(prompt);
        }
    }
    
    return parseFloat(value);
}

/**
 * Calculates the Lab Work Average
 * Formula: Lab Work Average = (Lab 1 + Lab 2 + Lab 3) / 3
 */
function calculateLabWorkAverage(lab1, lab2, lab3) {
    return (lab1 + lab2 + lab3) / 3.0;
}

/**
 * Calculates the Class Standing
 * Formula: Class Standing = (Attendance × 0.40) + (Lab Work Average × 0.60)
 */
function calculateClassStanding(attendance, labWorkAverage) {
    return (attendance * ATTENDANCE_WEIGHT) + (labWorkAverage * LAB_WORK_WEIGHT);
}

/**
 * Calculates the required Prelim Exam score to achieve a target grade
 * Formula: Prelim Exam = (Target Grade - Class Standing × 0.30) / 0.70
 */
function calculateRequiredExam(targetGrade, classStanding) {
    return (targetGrade - (classStanding * CLASS_STANDING_WEIGHT)) / PRELIM_EXAM_WEIGHT;
}

/**
 * Generates remarks based on required exam scores
 */
function generateRemarks(requiredPass, requiredExcellent, classStanding) {
    let remarks = "";
    
    if (requiredPass <= 0) {
        remarks = `✅ EXCELLENT: Your current Class Standing (${classStanding.toFixed(2)}%) is already sufficient for a Passing grade!\n` +
                  `Even a score of 0% on the Prelim Exam will result in a passing grade.\n\n` +
                  `🎯 RECOMMENDATION: Focus on maintaining your current performance.\n` +
                  `Any Prelim Exam score will improve your final grade.`;
    } else if (requiredExcellent <= 0) {
        remarks = `✅ VERY GOOD: Your current Class Standing (${classStanding.toFixed(2)}%) is very strong!\n` +
                  `You need at least ${requiredPass.toFixed(2)}% on the Prelim Exam to PASS.\n\n` +
                  `📌 NOTE: You will automatically achieve an Excellent grade regardless of Prelim Exam score.\n` +
                  `🎯 RECOMMENDATION: You're in excellent standing!`;
    } else if (requiredPass <= 50) {
        remarks = `✅ GOOD: Your current Class Standing (${classStanding.toFixed(2)}%) is solid.\n` +
                  `You need at least ${requiredPass.toFixed(2)}% on the Prelim Exam to PASS.\n` +
                  `You need at least ${requiredExcellent.toFixed(2)}% for EXCELLENT standing.\n\n` +
                  `🎯 RECOMMENDATION: Maintain your current performance and aim for a moderate Prelim score.`;
    } else if (requiredPass <= 75) {
        remarks = `⚠️  MODERATE: Your current Class Standing (${classStanding.toFixed(2)}%) needs some improvement.\n` +
                  `You need at least ${requiredPass.toFixed(2)}% on the Prelim Exam to PASS.\n` +
                  `You need at least ${requiredExcellent.toFixed(2)}% for EXCELLENT standing.\n\n` +
                  `🎯 RECOMMENDATION: Focus on achieving a solid Prelim Exam score to ensure passing.`;
    } else {
        remarks = `❌ CRITICAL: Your current Class Standing (${classStanding.toFixed(2)}%) is very low.\n` +
                  `You need at least ${requiredPass.toFixed(2)}% on the Prelim Exam to PASS.\n` +
                  `You need at least ${requiredExcellent.toFixed(2)}% for EXCELLENT standing.\n\n` +
                  `🎯 RECOMMENDATION: Prioritize your Prelim Exam preparation immediately!`;
    }
    
    return remarks;
}

/**
 * Main calculation function
 */
function calculatePrelimGrade() {
    console.clear();
    console.log("╔════════════════════════════════════════════════╗");
    console.log("║     PRELIM GRADE CALCULATOR - Lab Work 3       ║");
    console.log("║          JavaScript Console Version            ║");
    console.log("╚════════════════════════════════════════════════╝");
    console.log();
    
    // Input collection
    const attendance = getInputConsole("Enter your attendance score (0-100):");
    const lab1 = getInputConsole("Enter Lab Work 1 grade (0-100):");
    const lab2 = getInputConsole("Enter Lab Work 2 grade (0-100):");
    const lab3 = getInputConsole("Enter Lab Work 3 grade (0-100):");
    
    // Computations
    const labWorkAverage = calculateLabWorkAverage(lab1, lab2, lab3);
    const classStanding = calculateClassStanding(attendance, labWorkAverage);
    
    // Required Prelim Exam scores
    let requiredExamPass = calculateRequiredExam(PASSING_GRADE, classStanding);
    let requiredExamExcellent = calculateRequiredExam(EXCELLENT_GRADE, classStanding);
    
    // Clamp values to valid range (0-100)
    requiredExamPass = Math.max(0, Math.min(100, requiredExamPass));
    requiredExamExcellent = Math.max(0, Math.min(100, requiredExamExcellent));
    
    // Display results
    console.log("╔════════════════════════════════════════════════╗");
    console.log("║                COMPUTATION RESULTS              ║");
    console.log("╚════════════════════════════════════════════════╝");
    console.log();
    
    console.log("📊 INPUT SUMMARY:");
    console.log(`  • Attendance Score:  ${attendance.toFixed(2)}`);
    console.log(`  • Lab Work 1:        ${lab1.toFixed(2)}`);
    console.log(`  • Lab Work 2:        ${lab2.toFixed(2)}`);
    console.log(`  • Lab Work 3:        ${lab3.toFixed(2)}`);
    console.log();
    
    console.log("📈 COMPUTED VALUES:");
    console.log(`  • Lab Work Average:  ${labWorkAverage.toFixed(2)}`);
    console.log(`  • Class Standing:    ${classStanding.toFixed(2)}%`);
    console.log();
    
    console.log("📋 REQUIRED PRELIM EXAM SCORES:");
    console.log(`  • For PASSING (75):  ${requiredExamPass.toFixed(2)}%`);
    console.log(`  • For EXCELLENT (100): ${requiredExamExcellent.toFixed(2)}%`);
    console.log();
    
    console.log("╔════════════════════════════════════════════════╗");
    console.log("║              ACADEMIC STANDING REMARKS          ║");
    console.log("╚════════════════════════════════════════════════╝");
    console.log();
    
    console.log(generateRemarks(requiredExamPass, requiredExamExcellent, classStanding));
    
    console.log();
    console.log("💡 Grading Breakdown Reference:");
    console.log("  • Prelim Exam: 70% (Required score shown above)");
    console.log("  • Class Standing: 30% (Attendance 40% + Lab Works 60%)");
    console.log();
    console.log("✓ Program completed successfully.");
    console.log();
    console.log("═══════════════════════════════════════════════════");
}

// Run the program
calculatePrelimGrade();