package votee.models

/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

/**
 * Candidate represents the base contract of an election candidate implementation.
 * Users should extend this trait to implement a concrete candidate.
 * 
 * @param id the candidates id
 * @see [[PreferentialCandidate]]
 */
trait Candidate(val id: String)

/**
 * A concrete implementation of the candidate contract. Used internally for testing.
 * 
 * @param id the candidates id
 * @param name the candidates name
 * @param party candidates party if available
 */
private[votee] final case class PreferentialCandidate(
                                                       override val id: String,
                                                       name: String,
                                                       party: Option[String] = None
                                                     ) extends Candidate(id)