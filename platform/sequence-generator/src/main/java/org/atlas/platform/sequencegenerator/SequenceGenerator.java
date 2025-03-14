package org.atlas.platform.sequencegenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SequenceGenerator {

  private JdbcTemplate jdbcTemplate;

  @Transactional
  public String generate(SequenceType sequenceType) {
    // Step 1: Atomic update and get new sequence in one query
    jdbcTemplate.update(
        """
            UPDATE sequence_generator 
            SET seq_value = LAST_INSERT_ID(seq_value + 1) 
            WHERE seq_name = ?
            """,
        sequenceType.getName()
    );

    // Step 2: Fetch the new sequence value using LAST_INSERT_ID()
    Long sequence = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

    // Step 3: Format and return the generated code
    return generateFormattedString(sequenceType.getPrefix(), sequence, sequenceType.getPadding());
  }

  private String generateFormattedString(String prefix, Long sequence, int padding) {
    // Create a StringBuilder for efficient string concatenation
    StringBuilder builder = new StringBuilder(prefix);

    // Calculate the number of leading zeros needed
    int leadingZeros = padding - String.valueOf(sequence).length();

    // Append leading zeros if needed
    builder.append("0".repeat(Math.max(0, leadingZeros)));

    // Append the sequence number
    builder.append(sequence);

    return builder.toString();
  }
}
