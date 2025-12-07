package hpms.util;

import java.io.File;
import java.nio.file.Files;
import java.util.Locale;

/**
 * Small heuristic-based extractor used by the demo to auto-fill a test summary
 * from the file name or visible text content (best-effort). Not a production AI.
 */
public class TestSummaryExtractor {
    public static String extractSummary(String filePath, String type) {
        if (filePath == null) return null;
        String lower = new File(filePath).getName().toLowerCase(Locale.ROOT);
        StringBuilder sb = new StringBuilder();
        try {
            // Try quick read of textual content for small files
            java.nio.file.Path p = new File(filePath).toPath();
            if (Files.exists(p) && Files.size(p) < 16_384) {
                try { String content = new String(Files.readAllBytes(p)); lower += " " + content.toLowerCase(Locale.ROOT); } catch (Exception ex) { /* ignore */ }
            }
        } catch (Exception ex) { }

        if (type == null) type = "any";
        if (type.equalsIgnoreCase("xray") || lower.contains("xray") || lower.matches(".*(fracture|broken|bone|pneumonia|mass|tumor).*")) {
            if (lower.contains("fracture") || lower.contains("fractured") || lower.contains("broken")) sb.append("Finding: Fracture/Broken Bone; ");
            if (lower.contains("pneumonia")) sb.append("Finding: Pneumonia; ");
            if (lower.contains("tumor") || lower.contains("mass")) sb.append("Finding: Tumor/Mass; ");
            if (lower.contains("left")) sb.append("Location: Left; "); if (lower.contains("right")) sb.append("Location: Right; ");
        }
        if (type.equalsIgnoreCase("stool") || lower.contains("stool") || lower.matches(".*(e\\.coli|parasite|occult|blood).*")) {
            if (lower.contains("e.coli") || lower.contains("ecoli")) sb.append("Bacteria: Abnormal (E. coli); ");
            if (lower.contains("parasite")) sb.append("Parasites: Present; ");
            if (lower.contains("occult") || lower.contains("occult blood") || lower.contains("blood")) sb.append("Occult Blood: Positive; ");
        }
        if (type.equalsIgnoreCase("urine") || lower.contains("urine") || lower.matches(".*(cloudy|wbc|protein|glucose|uti).*")) {
            if (lower.contains("cloudy")) sb.append("Color/Clarity: Cloudy; ");
            if (lower.contains("wbc")) sb.append("WBC: High; ");
            if (lower.contains("protein")) sb.append("Protein: Positive; ");
            if (lower.contains("glucose")) sb.append("Glucose: Positive; ");
            if (lower.contains("uti")) sb.append("Interpretation: UTI suspected; ");
        }
        if (type.equalsIgnoreCase("blood") || lower.contains("cbc") || lower.matches(".*(wbc|crp|esr|hgb|hemoglobin|platelet|anemia|creatinine|alt|liver).*")) {
            if (lower.contains("wbc")) sb.append("WBC: Abnormal; ");
            if (lower.contains("crp") || lower.contains("esr")) sb.append("CRP/ESR: Elevated; ");
            if (lower.contains("hemoglobin") || lower.contains("hgb") || lower.contains("anemia")) sb.append("HGB: Anemic; ");
            if (lower.contains("platelet") || lower.contains("plt")) sb.append("PLT: Abnormal; ");
            if (lower.contains("creatinine") || lower.contains("kidney")) sb.append("Kidney marker: abnormal; ");
        }

        String s = sb.toString().trim();
        if (s.isEmpty()) return null;
        return s;
    }
}
