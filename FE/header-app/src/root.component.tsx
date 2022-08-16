import { AliwangwangFilled } from "@ant-design/icons";
import { Layout, Input, Button } from "antd";
import { BrowserRouter } from "react-router-dom";

const { Header } = Layout;
const { Search } = Input;

export default function Root(props: any) {
    return (
        <BrowserRouter>
            <Header
                style={{
                    position: "fixed",
                    zIndex: 1,
                    width: "100%",
                    top: 0,
                    background: "#FFFFFF",
                    boxShadow: "0 1px 1px rgba(0,0,0,0.1)",
                }}
            >
                <div
                    className="logo"
                    style={{ display: "flex", alignItems: "center", height: "100%", justifyContent: "space-between" }}
                >
                    <div style={{ display: "flex", alignItems: "center", width: "50%" }}>
                        <AliwangwangFilled style={{ fontSize: "50px", marginRight: "20px" }} />
                        <Search placeholder="Search..." loading={false} style={{ maxWidth: "420px" }} size="large" />
                    </div>
                    <div>
                        <Button style={{ marginRight: "20px" }}>Log in</Button>
                        <Button type="primary">Create account</Button>
                    </div>
                </div>
            </Header>
        </BrowserRouter>
    );
}
