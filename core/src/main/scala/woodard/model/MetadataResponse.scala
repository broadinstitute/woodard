/**
 * CromIam
 * Cromwell's Bouncer
 *
 * OpenAPI spec version: 
 * Contact: dsde@broadinstitute.org
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package woodard.model

import java.util.Date

case class MetadataResponse(
  // Mapping of input fully qualified names to stringified values
  inputs: Map[String, String],
  // Status in Cromwell execution terms.
  executionStatus: String,
  // The type of backend on which the call executed (e.g. JES, SGE, Local)
  backend: Option[String] = None,
  // Status in backend-specific terms.  Currently this will only be defined for the JES backend.
  backendStatus: Option[String] = None,
  // Start datetime of the call execution in ISO8601 format with milliseconds
  start: Option[Date] = None,
  // End datetime of the call execution in ISO8601 format with milliseconds
  end: Option[Date] = None,
  // Backend-specific job ID
  jobId: Option[String] = None,
  failures: Option[FailureMessage] = None,
  // Call execution return code
  returnCode: Option[Integer] = None,
  // Path to the standard output file for this call
  stdout: Option[String] = None,
  // Path to the standard error file for this call
  stderr: Option[String] = None,
  // Paths to backend specific logs for this call
  backendLogs: Option[Map[String, String]] = None
)

