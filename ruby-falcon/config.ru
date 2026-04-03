#!/usr/bin/env ruby

require 'ffi'

module Exprtk
  extend FFI::Library
  ffi_lib 'c-exprtk-adapter'
  attach_function :calculate, [:string], :double
end

WELCOME = "Welcome to calc service\nHTTP POST your expression\n"

module CalcApp
  def self.call(env)
    case env['REQUEST_METHOD']
    when 'GET'
      [200, {'Content-Type' => 'text/plain'}, [WELCOME]]
    when 'POST'
      expr = env['rack.input'].read
      result = Exprtk.calculate(expr)
      [200, {'Content-Type' => 'text/plain'}, [result.to_s]]
    else
      [405, {'Content-Type' => 'text/plain'}, ['Method Not Allowed']]
    end
  end
end

run CalcApp
