package skuber.model

import org.specs2.mutable.Specification // for unit-style testing

import scala.math.BigInt

import Model._

/**
 * @author David O'Riordan
 */
class ModelSpec extends Specification {
  "This is a unit specification for the skuber data model. ".txt
  
  
  // Pod(s)
  "A Pods type can be constructed from lists of Pods\n" >> {
    "where an empty list of pods can be used" >> {
      val podList = List[Pod]()
      val pods=Pods(metadata=ListMeta(), items = podList)
      pods.items.size mustEqual 0
    }
    
    "where a list with a single pod can be used" >> {
      val container1=Container(name="Container1","image1")
      val container2=Container(name="Container2","image2")
      val podspec=Pod.Spec(List(container1, container2))
      val pod=Pod.forNameAndSpec("MyPod", podspec)
      val podList = List[Pod](pod)
      val pods=Pods(metadata=ListMeta(), items = podList)
      pods.items.size mustEqual 1
      pods.items(0).metadata.name mustEqual "MyPod"
    }    
  }   
   // ReplicationController(s)
  "A ReplicationControllers type can be constructed from a list of ReplicationController (a.k.a. RC)\n" >> {
    "where an empty list of RCs can be used" >> {
      val rcList = List[ReplicationController]()
      val rcs=ReplicationControllers(items = rcList)
      rcs.items.size mustEqual 0
    }
    
    "where a list with a single RC can be used" >> {
      val container1=Container("Container1","image1")
      val container2=Container(name="Container2", "image2")
      val podspec=Pod.Spec(List(container1, container2))
      val rc = ReplicationController().withName("MyRepCon").withReplicas(2).withPodSpec(podspec)
      val rcList = List[ReplicationController](rc)
      val rcs=ReplicationControllers(items = rcList)
      rcs.items.size mustEqual 1
      rcs.items(0).metadata.name mustEqual "MyRepCon"
      rcs(0).name mustEqual "MyRepCon"
      rcs(0).spec.get.replicas mustEqual Some(2)
      val pspec = for (
          rcspec <- rcs(0).spec;
          tmpl <- rcspec.template;
          podspec <- tmpl.spec
      ) yield(podspec)
      pspec.get.containers.size mustEqual 2
    }    
  }   
}