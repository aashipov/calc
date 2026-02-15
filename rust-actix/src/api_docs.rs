use utoipa::OpenApi;

#[derive(OpenApi)]
#[openapi(
    paths(
        super::handler::index,
        super::handler::via_exprkt,
        super::handler::via_meval
    ))]
pub struct ApiDoc;
