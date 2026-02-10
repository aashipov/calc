use utoipa::OpenApi;

#[derive(OpenApi)]
#[openapi(
    paths(
        super::handler::index,
        super::handler::via_exprkt_rs,
        super::handler::via_meval
    ))]
pub struct ApiDoc;
