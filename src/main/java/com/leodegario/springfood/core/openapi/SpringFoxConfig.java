package com.leodegario.springfood.core.openapi;

import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leodegario.springfood.api.exceptionhandler.Problem;
import com.leodegario.springfood.api.model.CozinhaModel;
import com.leodegario.springfood.api.model.PedidoResumoModel;
import com.leodegario.springfood.api.openapi.model.CozinhasModelOpenApi;
import com.leodegario.springfood.api.openapi.model.PageableModelOpenApi;
import com.leodegario.springfood.api.openapi.model.PedidosResumoModelOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.ServletWebRequest;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig {

    @Bean
    public JacksonModuleRegistrar springFoxJacksonConfig() {
        return objectMapper -> objectMapper.registerModule(new JavaTimeModule());
    }

    @Bean
    public Docket apiDocket() {
        TypeResolver typeResolver = new TypeResolver();

        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.leodegario.springfood.api"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, globalGetResponseMessages())
                .globalResponses(HttpMethod.POST, globalPostPutResponseMessages())
                .globalResponses(HttpMethod.PUT, globalPostPutResponseMessages())
                .globalResponses(HttpMethod.DELETE, globalDeleteResponseMessages())
                .additionalModels(typeResolver.resolve(Problem.class))
                .ignoredParameterTypes(ServletWebRequest.class)
                .directModelSubstitute(Pageable.class, PageableModelOpenApi.class)
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(Page.class, CozinhaModel.class),
                        CozinhasModelOpenApi.class
                ))
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(Page.class, PedidoResumoModel.class),
                        PedidosResumoModelOpenApi.class))
                .apiInfo(apiInfo())
                .tags(new Tag("Cidades", "Gerencia as cidades"),
                        new Tag("Grupos", "Gerencia os grupos de usuários"),
                        new Tag("Cozinhas", "Gerencia as cozinhas"),
                        new Tag("Formas de pagamento", "Gerencia as formas de pagamento"),
                        new Tag("Pedidos", "Gerencia os pedidos"),
                        new Tag("Restaurantes", "Gerencia os restaurantes"),
                        new Tag("Estados", "Gerencia os estados"),
                        new Tag("Produtos", "Gerencia os produtos de restaurantes"));
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringFood API")
                .description("Api for clients and restaurants")
                .version("1")
                .contact(new Contact("Springfood", "http://springfood.com", "contact@springfood.com"))
                .build();
    }

    private List<Response> globalGetResponseMessages() {
        return Arrays.asList(
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Erro interno do Servidor")
                        .representation(MediaType.APPLICATION_JSON)
                        .apply(getProblemaModelReference())
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
                        .description("Recurso não possui representação que pode ser aceita pelo consumidor")
                        .build()
        );
    }

    private List<Response> globalPostPutResponseMessages() {
        return Arrays.asList(
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .description("Requisição inválida (erro do cliente)")
                        .representation(MediaType.APPLICATION_JSON)
                        .apply(getProblemaModelReference())
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Erro interno no servidor")
                        .representation(MediaType.APPLICATION_JSON)
                        .apply(getProblemaModelReference())
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
                        .description("Recurso não possui representação que poderia ser aceita pelo consumidor")
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
                        .description("Requisição recusada porque o corpo está em um formato não suportado")
                        .representation(MediaType.APPLICATION_JSON)
                        .apply(getProblemaModelReference())
                        .build()
        );
    }

    private List<Response> globalDeleteResponseMessages() {
        return Arrays.asList(
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .description("Requisição inválida (erro do cliente)")
                        .representation(MediaType.APPLICATION_JSON)
                        .apply(getProblemaModelReference())
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Erro interno no servidor")
                        .representation(MediaType.APPLICATION_JSON)
                        .apply(getProblemaModelReference())
                        .build()
        );
    }

    private Consumer<RepresentationBuilder> getProblemaModelReference() {
        return r -> r.model(m -> m.name("Problema")
                .referenceModel(ref -> ref.key(k -> k.qualifiedModelName(
                        q -> q.name("Problema").namespace("com.leodegario.springfood.api.exceptionhandler")))));
    }

}
