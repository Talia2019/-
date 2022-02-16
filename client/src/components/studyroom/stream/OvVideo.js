import React, { Component } from "react";
import "./StreamComponent.css";
export default class OvVideoComponent extends Component {
  constructor(props) {
    super(props);
    this.videoRef = React.createRef();
  }

  componentDidMount() {
    if (this.props && this.props.user.streamManager && !!this.videoRef) {
      console.log("PROPS: ", this.props);
      this.props.user.getStreamManager().addVideoElement(this.videoRef.current);
    }

    if (this.props && this.props.user.streamManager.session && this.props.user && !!this.videoRef) {
      this.props.user.streamManager.session.on("signal:userChanged", (event) => {
        const data = JSON.parse(event.data);
        if (data.isScreenShareActive !== undefined) {
          this.props.user.getStreamManager().addVideoElement(this.videoRef.current);
        }
      });
    }
  }

  componentDidUpdate(props) {
    // console.log("videl");
    // console.log(this.props.user);
    // console.log(this.props.mutedSound);
    if (props && !!this.videoRef) {
      this.props.user.getStreamManager().addVideoElement(this.videoRef.current);
    }
  }

  render() {
    return (
      <video
        autoPlay={this.props.user.isBlocked ? true : false}
        id={"video-" + this.props.user.getStreamManager().stream.streamId}
        ref={this.videoRef}
        muted={this.props.mutedSound}
      />
    );
  }
}
