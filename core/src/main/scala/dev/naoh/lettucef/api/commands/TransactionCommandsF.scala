// Code generated. DO NOT EDIT
package dev.naoh.lettucef.api.commands



trait TransactionCommandsF[F[_], K, V] {

  def discard(): F[String]
  
  def exec(): F[Boolean]
  
  def multi(): F[String]
  
  def watch(keys: K*): F[String]
  
  def unwatch(): F[String]
  
}
