use utoipa::OpenApi;

#[derive(OpenApi)]
#[openapi(
    paths(
        super::handler::welcome,
        super::handler::via_exprkt,
        super::handler::via_meval
    ),
    tags((description = "Calc")))]
pub struct ApiDoc;
