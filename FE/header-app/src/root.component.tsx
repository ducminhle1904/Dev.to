import { Layout, Menu } from "antd";
import { BrowserRouter } from "react-router-dom";
import { navigateToUrl } from "single-spa";

const { Header } = Layout;
const items = [
    { label: "React app", key: "/react" }, // remember to pass the key prop
    { label: "Angular app", key: "/angular" }, // which is required
];

export default function Root(props: any) {
    function onChangeRoute(event: any) {
        return navigateToUrl(event.key);
    }
    return (
        <BrowserRouter>
            <Header style={{ position: "fixed", zIndex: 1, width: "100%", top: 0 }}>
                <div className="logo" />
                <Menu
                    theme="dark"
                    mode="horizontal"
                    defaultSelectedKeys={["1"]}
                    items={items}
                    onClick={onChangeRoute}
                />
            </Header>
        </BrowserRouter>
    );
}
