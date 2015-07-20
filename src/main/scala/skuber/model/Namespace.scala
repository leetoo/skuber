package skuber.model

import Model._

/**
 * @author David O'Riordan
 */
case class Namespace(
  	val kind: String ="Namespace",
    override val apiVersion: String = "v1",
    val metadata: ObjectMeta,
    spec: Option[Namespace.Spec] = None,
    status: Option[Namespace.Status]= None) 
  extends ObjectResource {
  def meta(name: String): ObjectMeta = ObjectMeta(name, Some(this))
  def pod(name: String, spec: Pod.Spec) = Pod(metadata=meta(name), spec=spec)
  def node(name: String, spec: Option[Node.Spec] = None) = Node(metadata=meta(name), spec=spec)
  def replicationController(name: String, spec: Option[ReplicationController.Spec] = None) = 
    ReplicationController(metadata=meta(name), spec=spec)
  def withFinalizers(f: List[String]) = { this.copy(spec = Some(Namespace.Spec(f))) } 
  def withStatusOfPhase(p: String) =  { this.copy(status = Some(Namespace.Status(p))) } 
}

object Namespace {
  case class Spec(finalizers: List[String])
  case class Status(phase: String)
 
  val default = Namespace.forName("default")
  def forName(label: String) : Namespace = Namespace(metadata=ObjectMeta(name=label))
  def from(meta:ObjectMeta) : Namespace = Namespace(metadata=meta)
}