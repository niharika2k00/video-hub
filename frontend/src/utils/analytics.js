import ReactGA from "react-ga4";

// Essential Google Analytics tracking for VideoHub
export const analytics = {
  // Video Events - Core to video platform
  trackVideoPlay: (videoId, videoTitle) => {
    try {
      ReactGA.event({
        category: "Video",
        action: "Play",
        label: videoTitle || `Video ${videoId}`,
        value: 1,
      });
    } catch (error) {
      console.error("Failed to track video play:", error);
    }
  },

  trackVideoComplete: (videoId, videoTitle) => {
    try {
      ReactGA.event({
        category: "Video",
        action: "Complete",
        label: videoTitle || `Video ${videoId}`,
        value: 1,
      });
    } catch (error) {
      console.error("Failed to track video complete:", error);
    }
  },

  trackVideoUpload: (videoTitle) => {
    try {
      ReactGA.event({
        category: "Video",
        action: "Upload",
        label: videoTitle,
        value: 1,
      });
    } catch (error) {
      console.error("Failed to track video upload:", error);
    }
  },

  // User Authentication - Essential for user journey
  trackSignUp: () => {
    try {
      ReactGA.event({
        category: "User",
        action: "Sign Up",
        value: 1,
      });
    } catch (error) {
      console.error("Failed to track sign up:", error);
    }
  },

  trackSignIn: () => {
    try {
      ReactGA.event({
        category: "User",
        action: "Sign In",
        value: 1,
      });
    } catch (error) {
      console.error("Failed to track sign in:", error);
    }
  },

  trackSignOut: () => {
    try {
      ReactGA.event({
        category: "User",
        action: "Sign Out",
        value: 1,
      });
    } catch (error) {
      console.error("Failed to track sign out:", error);
    }
  },

  // Error Tracking - Important for debugging
  trackError: (errorType, errorMessage) => {
    try {
      ReactGA.event({
        category: "Error",
        action: errorType,
        label: errorMessage,
        value: 1,
      });
    } catch (error) {
      console.error("Failed to track error:", error);
    }
  },

  // Page Engagement Tracking
  trackContactPageVisit: () => {
    try {
      ReactGA.event({
        category: "Page",
        action: "Contact Visit",
        label: "Contact Us Page",
        value: 1,
      });
    } catch (error) {
      console.error("Failed to track contact page visit:", error);
    }
  },

  // Video Interaction Tracking
  trackSharePopupOpen: (videoId, videoTitle) => {
    try {
      ReactGA.event({
        category: "Video",
        action: "Share Popup Open",
        label: videoTitle || `Video ${videoId}`,
        value: 1,
      });
    } catch (error) {
      console.error("Failed to track share popup open:", error);
    }
  },
};

export default analytics;
