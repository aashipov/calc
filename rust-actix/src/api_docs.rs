use utoipa::OpenApi;

#[derive(OpenApi)]
#[openapi(
    paths(
        crate::handler::respond_welcome,
        crate::handler::respond_via_exprkt,
        crate::handler::respond_via_meval
    ),
    tags((description = "Calc")))]
pub struct ApiDoc;
