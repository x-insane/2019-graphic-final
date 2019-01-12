import React from 'react';
import { Tabs } from 'antd';
import MainTab from "./MainTab";
import ImageTab from "./ImageTab";

const TabPane = Tabs.TabPane;

class MainPage extends React.Component {

    state = {
        activeKey: 'main',
        panes: [
            {
                title: '主页',
                content: null,
                key: 'main',
                closable: false
            }
        ]
    };

    componentDidMount() {
        this.setState({
            activeKey: 'main',
            panes: [
                {
                    title: '主页',
                    content: <MainTab changeTab={this.changeTab.bind(this)}/>,
                    key: 'main',
                    closable: false
                }
            ]
        })
    }

    onChange = (activeKey) => {
        this.setState({ activeKey });
    };

    onEdit = (targetKey, action) => {
        this[action](targetKey);
    };

    changeTab = key => {
        let exists = false;
        this.state.panes.forEach(item => {
            if (item.key === key) {
                exists = true;
                this.setState({
                    activeKey: key
                })
            }
        });
        if (!exists) {
            this.setState({
                panes: [...this.state.panes, {
                    title: key.substring(0, 6),
                    content: <ImageTab filename={key}/>,
                    key: key
                }],
                activeKey: key
            })
        }
        window.scrollTo(0, 0);
    };

    remove = (targetKey) => {
        let activeKey = this.state.activeKey;
        let lastIndex;
        this.state.panes.forEach((pane, i) => {
            if (pane.key === targetKey)
                lastIndex = i - 1;
        });
        const panes = this.state.panes.filter(pane => pane.key !== targetKey);
        if (lastIndex >= 0 && activeKey === targetKey)
            activeKey = panes[lastIndex].key;
        this.setState({ panes, activeKey });
    };

    render() {
        return (
            <Tabs
                onChange={this.onChange}
                activeKey={this.state.activeKey}
                type="editable-card"
                hideAdd={true}
                onEdit={this.onEdit}
            >
                {
                    this.state.panes.map(pane =>
                        <TabPane tab={pane.title}
                                 key={pane.key}
                                 closable={pane.closable}>
                        {pane.content}
                        </TabPane>
                    )}
            </Tabs>
        );
    }
}

export default MainPage;
