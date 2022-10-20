package votee.models

/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

class Candidate(val id: String)

final case class PreferentialCandidate(override val id: String, name: String, party: Option[String] = None) extends Candidate(id)