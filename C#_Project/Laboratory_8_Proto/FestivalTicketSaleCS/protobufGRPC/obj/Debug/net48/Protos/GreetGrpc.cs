// <auto-generated>
//     Generated by the protocol buffer compiler.  DO NOT EDIT!
//     source: Protos/greet.proto
// </auto-generated>
#pragma warning disable 0414, 1591
#region Designer generated code

using grpc = global::Grpc.Core;

namespace protobufGRPC {
  public static partial class FestivalSv
  {
    static readonly string __ServiceName = "greet.FestivalSv";

    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static void __Helper_SerializeMessage(global::Google.Protobuf.IMessage message, grpc::SerializationContext context)
    {
      #if !GRPC_DISABLE_PROTOBUF_BUFFER_SERIALIZATION
      if (message is global::Google.Protobuf.IBufferMessage)
      {
        context.SetPayloadLength(message.CalculateSize());
        global::Google.Protobuf.MessageExtensions.WriteTo(message, context.GetBufferWriter());
        context.Complete();
        return;
      }
      #endif
      context.Complete(global::Google.Protobuf.MessageExtensions.ToByteArray(message));
    }

    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static class __Helper_MessageCache<T>
    {
      public static readonly bool IsBufferMessage = global::System.Reflection.IntrospectionExtensions.GetTypeInfo(typeof(global::Google.Protobuf.IBufferMessage)).IsAssignableFrom(typeof(T));
    }

    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static T __Helper_DeserializeMessage<T>(grpc::DeserializationContext context, global::Google.Protobuf.MessageParser<T> parser) where T : global::Google.Protobuf.IMessage<T>
    {
      #if !GRPC_DISABLE_PROTOBUF_BUFFER_SERIALIZATION
      if (__Helper_MessageCache<T>.IsBufferMessage)
      {
        return parser.ParseFrom(context.PayloadAsReadOnlySequence());
      }
      #endif
      return parser.ParseFrom(context.PayloadAsNewBuffer());
    }

    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static readonly grpc::Marshaller<global::protobufGRPC.Angajat> __Marshaller_greet_Angajat = grpc::Marshallers.Create(__Helper_SerializeMessage, context => __Helper_DeserializeMessage(context, global::protobufGRPC.Angajat.Parser));
    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static readonly grpc::Marshaller<global::protobufGRPC.LogInResponse> __Marshaller_greet_LogInResponse = grpc::Marshallers.Create(__Helper_SerializeMessage, context => __Helper_DeserializeMessage(context, global::protobufGRPC.LogInResponse.Parser));
    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static readonly grpc::Marshaller<global::protobufGRPC.FestivalResponse> __Marshaller_greet_FestivalResponse = grpc::Marshallers.Create(__Helper_SerializeMessage, context => __Helper_DeserializeMessage(context, global::protobufGRPC.FestivalResponse.Parser));
    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static readonly grpc::Marshaller<global::protobufGRPC.Bilet> __Marshaller_greet_Bilet = grpc::Marshallers.Create(__Helper_SerializeMessage, context => __Helper_DeserializeMessage(context, global::protobufGRPC.Bilet.Parser));
    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static readonly grpc::Marshaller<global::protobufGRPC.SpectacolDTO> __Marshaller_greet_SpectacolDTO = grpc::Marshallers.Create(__Helper_SerializeMessage, context => __Helper_DeserializeMessage(context, global::protobufGRPC.SpectacolDTO.Parser));
    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static readonly grpc::Marshaller<global::protobufGRPC.FestivalRequest> __Marshaller_greet_FestivalRequest = grpc::Marshallers.Create(__Helper_SerializeMessage, context => __Helper_DeserializeMessage(context, global::protobufGRPC.FestivalRequest.Parser));
    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static readonly grpc::Marshaller<global::protobufGRPC.StreamRequest> __Marshaller_greet_StreamRequest = grpc::Marshallers.Create(__Helper_SerializeMessage, context => __Helper_DeserializeMessage(context, global::protobufGRPC.StreamRequest.Parser));

    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static readonly grpc::Method<global::protobufGRPC.Angajat, global::protobufGRPC.LogInResponse> __Method_login = new grpc::Method<global::protobufGRPC.Angajat, global::protobufGRPC.LogInResponse>(
        grpc::MethodType.Unary,
        __ServiceName,
        "login",
        __Marshaller_greet_Angajat,
        __Marshaller_greet_LogInResponse);

    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static readonly grpc::Method<global::protobufGRPC.Angajat, global::protobufGRPC.FestivalResponse> __Method_logout = new grpc::Method<global::protobufGRPC.Angajat, global::protobufGRPC.FestivalResponse>(
        grpc::MethodType.Unary,
        __ServiceName,
        "logout",
        __Marshaller_greet_Angajat,
        __Marshaller_greet_FestivalResponse);

    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static readonly grpc::Method<global::protobufGRPC.Bilet, global::protobufGRPC.FestivalResponse> __Method_sellTicket = new grpc::Method<global::protobufGRPC.Bilet, global::protobufGRPC.FestivalResponse>(
        grpc::MethodType.Unary,
        __ServiceName,
        "sellTicket",
        __Marshaller_greet_Bilet,
        __Marshaller_greet_FestivalResponse);

    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static readonly grpc::Method<global::protobufGRPC.SpectacolDTO, global::protobufGRPC.FestivalResponse> __Method_getSpectacole = new grpc::Method<global::protobufGRPC.SpectacolDTO, global::protobufGRPC.FestivalResponse>(
        grpc::MethodType.Unary,
        __ServiceName,
        "getSpectacole",
        __Marshaller_greet_SpectacolDTO,
        __Marshaller_greet_FestivalResponse);

    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static readonly grpc::Method<global::protobufGRPC.FestivalRequest, global::protobufGRPC.FestivalResponse> __Method_getArtisti = new grpc::Method<global::protobufGRPC.FestivalRequest, global::protobufGRPC.FestivalResponse>(
        grpc::MethodType.Unary,
        __ServiceName,
        "getArtisti",
        __Marshaller_greet_FestivalRequest,
        __Marshaller_greet_FestivalResponse);

    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    static readonly grpc::Method<global::protobufGRPC.StreamRequest, global::protobufGRPC.FestivalResponse> __Method_NotifyStream = new grpc::Method<global::protobufGRPC.StreamRequest, global::protobufGRPC.FestivalResponse>(
        grpc::MethodType.DuplexStreaming,
        __ServiceName,
        "NotifyStream",
        __Marshaller_greet_StreamRequest,
        __Marshaller_greet_FestivalResponse);

    /// <summary>Service descriptor</summary>
    public static global::Google.Protobuf.Reflection.ServiceDescriptor Descriptor
    {
      get { return global::protobufGRPC.GreetReflection.Descriptor.Services[0]; }
    }

    /// <summary>Base class for server-side implementations of FestivalSv</summary>
    [grpc::BindServiceMethod(typeof(FestivalSv), "BindService")]
    public abstract partial class FestivalSvBase
    {
      [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
      public virtual global::System.Threading.Tasks.Task<global::protobufGRPC.LogInResponse> login(global::protobufGRPC.Angajat request, grpc::ServerCallContext context)
      {
        throw new grpc::RpcException(new grpc::Status(grpc::StatusCode.Unimplemented, ""));
      }

      [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
      public virtual global::System.Threading.Tasks.Task<global::protobufGRPC.FestivalResponse> logout(global::protobufGRPC.Angajat request, grpc::ServerCallContext context)
      {
        throw new grpc::RpcException(new grpc::Status(grpc::StatusCode.Unimplemented, ""));
      }

      [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
      public virtual global::System.Threading.Tasks.Task<global::protobufGRPC.FestivalResponse> sellTicket(global::protobufGRPC.Bilet request, grpc::ServerCallContext context)
      {
        throw new grpc::RpcException(new grpc::Status(grpc::StatusCode.Unimplemented, ""));
      }

      [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
      public virtual global::System.Threading.Tasks.Task<global::protobufGRPC.FestivalResponse> getSpectacole(global::protobufGRPC.SpectacolDTO request, grpc::ServerCallContext context)
      {
        throw new grpc::RpcException(new grpc::Status(grpc::StatusCode.Unimplemented, ""));
      }

      [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
      public virtual global::System.Threading.Tasks.Task<global::protobufGRPC.FestivalResponse> getArtisti(global::protobufGRPC.FestivalRequest request, grpc::ServerCallContext context)
      {
        throw new grpc::RpcException(new grpc::Status(grpc::StatusCode.Unimplemented, ""));
      }

      [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
      public virtual global::System.Threading.Tasks.Task NotifyStream(grpc::IAsyncStreamReader<global::protobufGRPC.StreamRequest> requestStream, grpc::IServerStreamWriter<global::protobufGRPC.FestivalResponse> responseStream, grpc::ServerCallContext context)
      {
        throw new grpc::RpcException(new grpc::Status(grpc::StatusCode.Unimplemented, ""));
      }

    }

    /// <summary>Creates service definition that can be registered with a server</summary>
    /// <param name="serviceImpl">An object implementing the server-side handling logic.</param>
    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    public static grpc::ServerServiceDefinition BindService(FestivalSvBase serviceImpl)
    {
      return grpc::ServerServiceDefinition.CreateBuilder()
          .AddMethod(__Method_login, serviceImpl.login)
          .AddMethod(__Method_logout, serviceImpl.logout)
          .AddMethod(__Method_sellTicket, serviceImpl.sellTicket)
          .AddMethod(__Method_getSpectacole, serviceImpl.getSpectacole)
          .AddMethod(__Method_getArtisti, serviceImpl.getArtisti)
          .AddMethod(__Method_NotifyStream, serviceImpl.NotifyStream).Build();
    }

    /// <summary>Register service method with a service binder with or without implementation. Useful when customizing the  service binding logic.
    /// Note: this method is part of an experimental API that can change or be removed without any prior notice.</summary>
    /// <param name="serviceBinder">Service methods will be bound by calling <c>AddMethod</c> on this object.</param>
    /// <param name="serviceImpl">An object implementing the server-side handling logic.</param>
    [global::System.CodeDom.Compiler.GeneratedCode("grpc_csharp_plugin", null)]
    public static void BindService(grpc::ServiceBinderBase serviceBinder, FestivalSvBase serviceImpl)
    {
      serviceBinder.AddMethod(__Method_login, serviceImpl == null ? null : new grpc::UnaryServerMethod<global::protobufGRPC.Angajat, global::protobufGRPC.LogInResponse>(serviceImpl.login));
      serviceBinder.AddMethod(__Method_logout, serviceImpl == null ? null : new grpc::UnaryServerMethod<global::protobufGRPC.Angajat, global::protobufGRPC.FestivalResponse>(serviceImpl.logout));
      serviceBinder.AddMethod(__Method_sellTicket, serviceImpl == null ? null : new grpc::UnaryServerMethod<global::protobufGRPC.Bilet, global::protobufGRPC.FestivalResponse>(serviceImpl.sellTicket));
      serviceBinder.AddMethod(__Method_getSpectacole, serviceImpl == null ? null : new grpc::UnaryServerMethod<global::protobufGRPC.SpectacolDTO, global::protobufGRPC.FestivalResponse>(serviceImpl.getSpectacole));
      serviceBinder.AddMethod(__Method_getArtisti, serviceImpl == null ? null : new grpc::UnaryServerMethod<global::protobufGRPC.FestivalRequest, global::protobufGRPC.FestivalResponse>(serviceImpl.getArtisti));
      serviceBinder.AddMethod(__Method_NotifyStream, serviceImpl == null ? null : new grpc::DuplexStreamingServerMethod<global::protobufGRPC.StreamRequest, global::protobufGRPC.FestivalResponse>(serviceImpl.NotifyStream));
    }

  }
}
#endregion
