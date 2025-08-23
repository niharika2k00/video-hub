import {
  Github,
  Linkedin,
  Twitter,
  Mail,
  Play,
  Upload,
  Users,
  Zap,
} from "lucide-react";

const Footer = () => {
  const year = new Date().getFullYear();

  const quickLinks = [
    { href: "/about", text: "About Us", hoverColor: "hover:text-yellow-200" },
    { href: "/contact", text: "Contact Us", hoverColor: "hover:text-pink-200" },
    { href: "#", text: "Privacy Policy", hoverColor: "hover:text-indigo-300" },
    { href: "#", text: "Terms of Service", hoverColor: "hover:text-green-300" },
    { href: "#", text: "Support", hoverColor: "hover:text-yellow-200" },
  ];

  const socialLinks = [
    {
      href: "https://github.com/niharika2k00",
      icon: Github,
      title: "GitHub",
      hoverColor: "hover:text-yellow-200",
      bgColor: "hover:bg-yellow-200/20",
    },
    {
      href: "https://www.linkedin.com/in/niharika2k00/",
      icon: Linkedin,
      title: "LinkedIn",
      hoverColor: "hover:text-pink-200",
      bgColor: "hover:bg-pink-200/20",
    },
    {
      href: "https://x.com/niharika_2k",
      icon: Twitter,
      title: "X",
      hoverColor: "hover:text-white",
      bgColor: "hover:bg-blue-200/20",
    },
    {
      href: "mailto:dniharika16@gmail.com",
      icon: Mail,
      title: "Email",
      hoverColor: "hover:text-green-300",
      bgColor: "hover:bg-green-300/20",
    },
  ];

  return (
    <footer className="bg-gradient-to-r from-blue-800 via-purple-800 to-indigo-900 text-white relative overflow-hidden">
      {/* Light Color Overlay */}
      <div className="absolute inset-0 bg-gradient-to-br from-blue-600/20 via-purple-600/20 to-pink-500/20"></div>

      {/* Floating Light Elements */}
      <div className="absolute top-10 left-10 w-20 h-20 bg-yellow-200/40 rounded-full blur-xl"></div>
      <div className="absolute top-20 right-20 w-16 h-16 bg-pink-200/40 rounded-full blur-xl"></div>
      <div className="absolute bottom-20 left-1/3 w-12 h-12 bg-blue-200/50 rounded-full blur-xl"></div>

      {/* Main Footer Content */}
      <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
          <div className="lg:col-span-2">
            <div className="flex items-center mb-4">
              <h3 className="text-2xl font-bold">
                <span className="text-yellow-400">VIDEO</span>HUB
              </h3>
            </div>
            <p className="text-blue-200 mb-6 max-w-md">
              Professional video hosting and streaming platform with advanced
              ABR technology. Upload once, stream everywhere with seamless HLS
              integration.
            </p>

            {/* Social Media Icons */}
            <div className="flex space-x-4">
              {socialLinks.map((social, index) => {
                const IconComponent = social.icon;
                return (
                  <a
                    key={index}
                    href={social.href}
                    target="_blank"
                    rel="noopener noreferrer"
                    className={`text-blue-200 transition-colors p-2 rounded-lg ${social.hoverColor} ${social.bgColor}`}
                    title={social.title}
                  >
                    <IconComponent className="w-5 h-5" />
                  </a>
                );
              })}
            </div>
          </div>

          {/* Features */}
          <div>
            <h4 className="text-lg font-semibold mb-4 text-yellow-100">
              Features
            </h4>
            <ul className="space-y-3 text-blue-200">
              <li className="flex items-center">
                <Upload className="w-4 h-4 mr-2 text-yellow-200" />
                One-Click Upload
              </li>
              <li className="flex items-center">
                <Play className="w-4 h-4 mr-2 text-pink-200" />
                Adaptive Streaming
              </li>
              <li className="flex items-center">
                <Zap className="w-4 h-4 mr-2 text-blue-200" />
                HLS Technology
              </li>
              <li className="flex items-center">
                <Users className="w-4 h-4 mr-2 text-green-200" />
                Global CDN
              </li>
            </ul>
          </div>

          {/* Quick Links */}
          <div>
            <h4 className="text-lg font-semibold mb-4 text-yellow-100">
              Quick Links
            </h4>
            <ul className="space-y-3 text-blue-200">
              {quickLinks.map((link, index) => (
                <li key={index}>
                  <a
                    href={link.href}
                    className={`transition-colors ${link.hoverColor}`}
                  >
                    {link.text}
                  </a>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>

      {/* Bottom Bar */}
      <div className="border-t border-white/20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
          <div className="flex flex-col md:flex-row items-center justify-between">
            <div className="text-blue-200 text-sm mb-4 md:mb-0">
              © {year} VideoHub. All rights reserved.
            </div>
            <div className="flex space-x-6 text-sm text-blue-200">
              <a href="#" className="hover:text-white transition-colors">
                Cookie Policy
              </a>
              <a href="#" className="hover:text-white transition-colors">
                Data Protection
              </a>
              <a href="#" className="hover:text-white transition-colors">
                Accessibility
              </a>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
