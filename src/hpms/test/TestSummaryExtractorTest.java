package hpms.test;

import hpms.util.TestSummaryExtractor;

public class TestSummaryExtractorTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Running TestSummaryExtractor quick checks...");
        String s1 = TestSummaryExtractor.extractSummary("patient_right_arm_fracture_xray.jpg", "xray");
        System.out.println("xray summary: " + s1);
        String s2 = TestSummaryExtractor.extractSummary("stool_ecoli_report.pdf", "stool");
        System.out.println("stool summary: " + s2);
        String s3 = TestSummaryExtractor.extractSummary("cloudy_urine_report.txt", "urine");
        System.out.println("urine summary: " + s3);
        String s4 = TestSummaryExtractor.extractSummary("cbc_wbc_elevated.txt", "blood");
        System.out.println("blood summary: " + s4);

        // Basic assertions by printing and checking not-all-null; exit non-zero on obvious failure
        if (s1 == null || s2 == null || s3 == null || s4 == null) {
            System.err.println("One or more extractor checks returned null — please inspect the heuristics.");
            System.exit(2);
        }
        System.out.println("All summary checks produced non-null results — heuristics appear functional.");
    }
}
